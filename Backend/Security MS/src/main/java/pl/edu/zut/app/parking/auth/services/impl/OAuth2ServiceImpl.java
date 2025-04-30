package pl.edu.zut.app.parking.auth.services.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.edu.zut.app.parking.auth.dto.OAuthTokenResponse;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.entities.ExternalAuthenticationProvider;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.exceptions.ForbiddenException;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;
import pl.edu.zut.app.parking.auth.kafka.RegistrationUserProducer;
import pl.edu.zut.app.parking.auth.services.GoogleJWTValidator;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.OAuth2Service;
import pl.edu.zut.app.parking.auth.utils.OAuth2Constants;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

  @Value("${oauth2.google.client-id}")
  String googleClientId;

  @Value("${oauth2.google.redirect-uri}")
  String googleClientRedirectUri;

  @Value("${oauth2.google.client-secret}")
  private String googleClientSecret;

  private final GoogleJWTValidator googleJWTValidator;
  private final JWTService jwtService;
  private final UserRepositoryServiceImpl userRepository;
  private final RegistrationUserProducer registrationUserProducer;
  private final WebClient webClient = WebClient.create();

  @Override
  public String getGoogleAuthenticatorUrl() {
    return OAuth2Constants.GOOGLE_AUTH_URL
        + "?client_id="
        + googleClientId
        + "&redirect_uri="
        + googleClientRedirectUri
        + "&scope="
        + URLEncoder.encode(OAuth2Constants.DEFAULT_SCOPE, StandardCharsets.UTF_8)
        + "&response_type=code"
        + "&access_type=offline";
  }

  @Override
  public String getAppleAuthenticatorUrl() {
    throw new UnsupportedOperationException(
        "Apple authenticator URL generation is not implemented yet.");
  }

  @Override
  public AuthenticationTokens handleGoogleAuthenticatorResponse(String code) {
    log.info("Handling Google OAuth response");
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("code", code);
    requestBody.put("client_id", googleClientId);
    requestBody.put("client_secret", googleClientSecret);
    requestBody.put("redirect_uri", googleClientRedirectUri);
    requestBody.put("grant_type", OAuth2Constants.AUTHORIZATION_CODE);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, headers);

    log.info("Google OAuth request body: {}", requestBody);

    OAuthTokenResponse response = getOAuthTokens(httpEntity, OAuth2Constants.GOOGLE_TOKEN_URL);
    log.info("Google OAuth response: {}", response);

    JWTClaimsSet claims = googleJWTValidator.verifyGoogleToken(response.id_token());
    User user = processOAuthLogin(claims);

    return jwtService.generateAuthenticationTokens(user);
  }

  @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
  public OAuthTokenResponse getOAuthTokens(
      HttpEntity<Map<String, String>> httpEntity, String tokenUri) {
    log.info("Getting OAuth tokens from {}", tokenUri);

    Map<String, String> requestBody =
        Optional.ofNullable(httpEntity.getBody())
            .orElseThrow(() -> new IllegalArgumentException("Request body cannot be null"));

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    requestBody.forEach(formData::add);

    return webClient
        .post()
        .uri(tokenUri)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .onStatus(
            status -> HttpStatus.valueOf(status.value()).is4xxClientError(),
            response -> Mono.error(new RuntimeException("Invalid request")))
        .onStatus(
            status -> HttpStatus.valueOf(status.value()).is5xxServerError(),
            response -> Mono.error(new RuntimeException("Server error")))
        .bodyToMono(OAuthTokenResponse.class)
        .block();
  }

  /**
   * Processes an OAuth login based on the provided JWT claims. This method handles both the login
   * of an existing user and the registration of a new user if no existing account matches the email
   * in the claims.
   *
   * @param claims the JWT claims containing user information, such as email and external provider
   *     details
   * @return the User object corresponding to the logged-in or newly registered user
   */
  private User processOAuthLogin(JWTClaimsSet claims) {
    log.info("Processing OAuth login");
    String email = extractEmail(claims);
    try {
      User user = userRepository.findByEmail(email);
      log.info("Processing existing user");
      validateUser(user);
      verifyExternalProvider(user, claims);
      return user;
    } catch (UserNotFoundException e) {
      log.info("Processing new user");
      return handleOAuthRegistration(claims);
    }
  }

  /**
   * Extracts the email address from the given JWT claims set.
   *
   * @param claims the JWTClaimsSet object containing the claims from which the email is to be
   *     extracted
   * @return the extracted email address as a string
   * @throws ForbiddenException if the email claim is not present in the JWT claims set
   */
  private String extractEmail(JWTClaimsSet claims) {
    log.info("Extracting email from JWT claims");
    return Optional.ofNullable(claims.getClaim("email"))
        .map(Object::toString)
        .orElseThrow(() -> new ForbiddenException("Email not found in JWT claims"));
  }

  /**
   * Validates the user's account status and authentication provider.
   *
   * @param user the user object to be validated. The method checks if the user account is locked,
   *     disabled, expired, or belongs to an unsupported external authentication provider. If any of
   *     these conditions are true, an exception is thrown.
   * @throws ForbiddenException if a user doesn't have permissions to do this action
   */
  private void validateUser(User user) {
    if (!user.isAccountNonLocked() || !user.isEnabled() || !user.isAccountNonExpired()) {
      log.warn("User is locked, disabled, or expired");
      throw new ForbiddenException("User is locked, disabled, or expired");
    }
    if (Boolean.FALSE.equals(user.getIsSupportedExternalAuthenticationProvider())) {
      log.warn("User is not supported external authentication provider");
      throw new ForbiddenException("User is not supported external authentication provider");
    }
  }

  /**
   * Verifies if the external authentication provider associated with the given user matches the
   * provided JWT claims. If no matching provider is found, a ForbiddenException is thrown.
   *
   * @param user The user whose external authentication providers are to be verified.
   * @param claims The JWT claims containing issuer and subject information for verification.
   * @throws ForbiddenException if no matching external authentication provider found
   */
  private void verifyExternalProvider(User user, JWTClaimsSet claims) {
    boolean hasMatchingProvider =
        user.getExternalAuthenticationProviders().stream()
            .anyMatch(provider -> provider.matches(claims.getIssuer(), claims.getSubject()));

    if (!hasMatchingProvider) {
      throw new ForbiddenException("No matching external authentication provider found");
    }
  }

  /**
   * Handles user registration through OAuth by extracting user claims from the provided JWT and
   * saving the user information to the repository. The method creates a User object with
   * information from the JWT and sends an event message for further processing.
   *
   * @param claims the JWTClaimsSet object containing claims extracted from the OAuth token
   * @return the registered User object saved in the repository
   */
  @Transactional
  private User handleOAuthRegistration(JWTClaimsSet claims) {
    log.info("Handling OAuth registration");
    log.info("User claims: {}", claims);

    String email = extractEmail(claims);
    String givenName = claims.getClaim("given_name").toString();
    String familyName = claims.getClaim("family_name").toString();
    String pictureUrl = claims.getClaim("picture").toString();
    Boolean emailVerified = (Boolean) claims.getClaim("email_verified");
    String sub = claims.getClaim("sub").toString();
    String iss = claims.getClaim("iss").toString();

    ExternalAuthenticationProvider externalAuthenticationProvider =
        ExternalAuthenticationProvider.builder()
            .issuer(iss)
            .subjectId(sub)
            .build();

    User user =
        User.builder()
            .email(email)
            .passwordHash(null)
            .userType(UserType.DRIVER)
            .userPossibilities(Possibilities.getPossibilitiesByUserType(UserType.DRIVER))
            .isSupportedExternalAuthenticationProvider(true)
            .isSupportingLoginByCredentials(false)
            .externalAuthenticationProviders(List.of(externalAuthenticationProvider))
            .build();

    user = userRepository.save(user);
    registrationUserProducer.sendDriverRegistrationMessage(
        user.getId(), email, givenName, familyName, pictureUrl, emailVerified, null);
    return user;
  }

  @Override
  public AuthenticationTokens handleAppleAuthenticatorResponse(String code) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
