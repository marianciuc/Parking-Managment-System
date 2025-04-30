package pl.edu.zut.app.parking.auth.services.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.exceptions.TokenValidationException;
import pl.edu.zut.app.parking.auth.services.GoogleJWTValidator;

@Slf4j
@Service
public class GoogleJWTValidatorImpl implements GoogleJWTValidator {

  @Value("${oauth2.google.client-id}")
  String googleClientId;

  @Value("${oauth2.google.jwks-url}")
  String jwksURL = "https://www.googleapis.com/oauth2/v3/certs";

  private final Cache<String, JWK> jwkCache =
      Caffeine.newBuilder().maximumSize(100).expireAfterWrite(1, TimeUnit.HOURS).build();

  /**
   * Verifies the provided Google ID token by checking its signature and claims.
   *
   * @param idToken the ID token to validate
   * @return the verified claims from the ID token
   * @throws TokenValidationException if the validation fails
   */
  @Override
  public JWTClaimsSet verifyGoogleToken(String idToken) {
    try {
      log.info("Verifying Google ID token");
      SignedJWT signedJWT = SignedJWT.parse(idToken);

      JWSVerifier verifier = new RSASSAVerifier(getGooglePublicKey(idToken));

      if (!signedJWT.verify(verifier)) {
        log.error("Invalid ID token signature");
        throw new IllegalArgumentException("Invalid ID token signature");
      }

      log.info("ID token verified successfully");
      return getJwtClaimsSet(signedJWT);
    } catch (Exception e) {
      log.error("Failed to verify Google ID token", e);
      throw new TokenValidationException("Failed to verify Google ID token", e);
    }
  }

  /**
   * Extracts and validates the claims set from a signed JSON Web Token (JWT).
   *
   * @param signedJWT the signed JWT from which to retrieve the claims set
   * @return the validated JWTClaimsSet extracted from the signed JWT
   * @throws ParseException if there is an issue parsing the JWT claims
   * @throws IllegalArgumentException if the audience, issuer, or expiration time of the JWT claims
   *     are invalid
   */
  private JWTClaimsSet getJwtClaimsSet(SignedJWT signedJWT) throws ParseException {
    log.info("Extracting and validating JWT claims");
    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
    if (!googleClientId.equals(claims.getAudience().getFirst())) {
      throw new IllegalArgumentException("Invalid audience in ID token");
    }

    if (!"https://accounts.google.com".equals(claims.getIssuer())) {
      throw new IllegalArgumentException("Invalid issuer in ID token");
    }

    if (claims.getExpirationTime().before(new Date())) {
      throw new IllegalArgumentException("ID token has expired");
    }
    return claims;
  }

  /**
   * Retrieves the Google public key for validating the signature of a Google ID token.
   *
   * @param idToken the Google ID token that contains the key ID in its header
   * @return the corresponding RSAPublicKey used to verify the signature of the ID token
   * @throws ParseException if there is an error parsing the ID token
   * @throws JOSEException if there is an error related to JOSE (JSON Object Signing and Encryption)
   *     operations
   * @throws MalformedURLException if the URL for the JWKS endpoint is malformed
   */
  private RSAPublicKey getGooglePublicKey(String idToken)
      throws ParseException, JOSEException, MalformedURLException {
    log.info("Retrieving Google public key");
    SignedJWT signedJWT = SignedJWT.parse(idToken);
    String kid = signedJWT.getHeader().getKeyID();

    JWK jwk = jwkCache.getIfPresent(kid);
    log.info("Retrieved Google public key: {}", jwk);
    if (jwk == null) {
      log.info("Google public key not found in cache, retrieving from JWKS endpoint");
      DefaultResourceRetriever resourceRetriever = new DefaultResourceRetriever(5000, 5000);
      JWKSource<SecurityContext> keySource =
          new RemoteJWKSet<>(new URL(jwksURL), resourceRetriever);
      jwk =
          keySource.get(new JWKSelector(new JWKMatcher.Builder().keyID(kid).build()), null).get(0);
      jwkCache.put(kid, jwk);
    }

    log.info("Converting Google public key to RSAPublicKey");
    return (RSAPublicKey) jwk.toRSAKey().toPublicKey();
  }
}
