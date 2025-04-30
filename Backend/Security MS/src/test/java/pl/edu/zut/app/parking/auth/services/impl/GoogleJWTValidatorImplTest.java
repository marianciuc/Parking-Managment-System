package pl.edu.zut.app.parking.auth.services.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.github.javafaker.Faker;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import pl.edu.zut.app.parking.auth.exceptions.TokenValidationException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class GoogleJWTValidatorImplTest {
  @InjectMocks private GoogleJWTValidatorImpl validator;

  private RSAKey rsaJWK;
  private KeyPair keyPair;
  private Faker faker;
  private String testClientId;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    faker = new Faker();
    testClientId = faker.internet().uuid();
    ReflectionTestUtils.setField(validator, "googleClientId", testClientId);

    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048);
    keyPair = generator.generateKeyPair();
    rsaJWK =
        new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
            .privateKey(keyPair.getPrivate())
            .keyID("testKid")
            .build();

    @SuppressWarnings("unchecked")
    Cache<String, Object> jwkCache =
        (Cache<String, Object>) ReflectionTestUtils.getField(validator, "jwkCache");
    jwkCache.put("testKid", rsaJWK);
  }

  /** Helper method to generate a signed JWT with specified claims. */
  @SneakyThrows
  private String generateSignedToken(
      String audience, String issuer, Date expirationTime, String kid, RSASSASigner signer) {
    JWTClaimsSet claims =
        new JWTClaimsSet.Builder()
            .audience(audience)
            .issuer(issuer)
            .expirationTime(expirationTime)
            .issueTime(new Date())
            .build();
    SignedJWT signedJWT =
        new SignedJWT(
            new com.nimbusds.jose.JWSHeader.Builder(JWSAlgorithm.RS256).keyID(kid).build(), claims);
    signedJWT.sign(signer);
    return signedJWT.serialize();
  }

  @Nested
  @DisplayName("Positive Scenarios")
  class PositiveTests {
    @Test
    @DisplayName("Should verify valid Google token successfully")
    void verifyGoogleToken_validToken_success(){
      // Arrange
      RSASSASigner signer = new RSASSASigner(keyPair.getPrivate());
      Date future = new Date(System.currentTimeMillis() + 3600 * 1000);
      String token =
          generateSignedToken(
              testClientId, "https://accounts.google.com", future, "testKid", signer);
      // Act
      JWTClaimsSet claims = validator.verifyGoogleToken(token);
      // Assert
      assertNotNull(claims);
      assertEquals(testClientId, claims.getAudience().get(0));
      assertEquals("https://accounts.google.com", claims.getIssuer());
    }
  }

  @Nested
  @DisplayName("Negative Scenarios")
  class NegativeTests {
    @Test
    @DisplayName("Should throw TokenValidationException for malformed token")
    void verifyGoogleToken_malformedToken_exception() {
      // Arrange
      String invalidToken = "this-is-not-a-valid-token";
      // Act
      Executable executable = () -> validator.verifyGoogleToken(invalidToken);
      // Assert
      TokenValidationException ex = assertThrows(TokenValidationException.class, executable);
      assertTrue(ex.getMessage().contains("Failed to verify Google ID token"));
    }

    @Test
    @DisplayName("Should throw exception when token signature is invalid")
    void verifyGoogleToken_invalidSignature_exception() throws Exception {
      // Arrange
      // Generate another key pair to sign the token and create a mismatch with cached key.
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048);
      KeyPair anotherKeyPair = generator.generateKeyPair();

      RSASSASigner wrongSigner = new RSASSASigner(anotherKeyPair.getPrivate());
      Date future = new Date(System.currentTimeMillis() + 3600 * 1000);
      String token =
          generateSignedToken(
              testClientId, "https://accounts.google.com", future, "testKid", wrongSigner);
      // Act
      Executable executable = () -> validator.verifyGoogleToken(token);
      // Assert
      TokenValidationException ex = assertThrows(TokenValidationException.class, executable);
      assertEquals("Failed to verify Google ID token", ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
      "wrong-client,https://accounts.google.com,  3600, 'Failed to verify Google ID token'",
      "FAKER_PLACEHOLDER,wrong-issuer,  3600, 'Failed to verify Google ID token'"
    })
    @DisplayName("Should throw exception for invalid audience or issuer")
    void verifyGoogleToken_invalidClaims_exception(
        String audience, String issuer, int expirySeconds, String expectedMessage) {
      // Arrange
      RSASSASigner signer = new RSASSASigner(keyPair.getPrivate());
      Date future = new Date(System.currentTimeMillis() + expirySeconds * 1000);
      // If audience placeholder is provided then use testClientId to avoid conflict
      if (audience.equals(FAKER_PLACEHOLDER)) {
        audience = testClientId;
      }
      String token = generateSignedToken(audience, issuer, future, "testKid", signer);
      // Act
      Exception ex = assertThrows(Exception.class, () -> validator.verifyGoogleToken(token));
      // Assert
      assertTrue(ex.getMessage().contains(expectedMessage));
    }

    @Test
    @DisplayName("Should throw exception for expired token")
    void verifyGoogleToken_expiredToken_exception() {
      // Arrange
      RSASSASigner signer = new RSASSASigner(keyPair.getPrivate());
      Date past = new Date(System.currentTimeMillis() - 3600 * 1000);
      String token =
          generateSignedToken(testClientId, "https://accounts.google.com", past, "testKid", signer);
      // Act
      Exception ex =
          assertThrows(TokenValidationException.class, () -> validator.verifyGoogleToken(token));
      // Assert
      assertTrue(ex.getMessage().contains("Failed to verify Google ID token"));
    }
  }

  @Nested
  @DisplayName("Parameterized Tests")
  class ParameterizedTokenTests {
    static Stream<TestTokenData> tokenDataProvider() throws Exception {
      Faker localFaker = new Faker();
      // Create valid test data
      Date future = new Date(System.currentTimeMillis() + 3600 * 1000);
      return Stream.of(
          new TestTokenData(
              localFaker.internet().uuid(),
              "https://accounts.google.com",
              future,
              "Failed to verify Google ID token"),
          new TestTokenData(
              localFaker.internet().uuid(),
              "wrong-issuer",
              future,
              "Failed to verify Google ID token"),
          new TestTokenData(
              localFaker.internet().uuid(),
              "wrong-issuer",
              new Date(System.currentTimeMillis() - 3600 * 1000),
              "Failed to verify Google ID token"));
    }

    @ParameterizedTest
    @MethodSource("tokenDataProvider")
    @DisplayName("Should fail verification with various invalid token claim scenarios")
    void verifyGoogleToken_invalidClaimsParameterized(TestTokenData data) throws Exception {
      // Arrange
      RSASSASigner signer = new RSASSASigner(data.privateKey);
      String token =
          generateSignedToken(data.audience, data.issuer, data.expiration, "testKid", signer);
      // Act & Assert
      Exception ex =
          assertThrows(TokenValidationException.class, () -> validator.verifyGoogleToken(token));
      log.info("Exception message: {}", ex.getMessage());
      assertTrue(ex.getMessage().contains(data.expectedErrorMessage));
    }
  }

  // Helper class for parameterized test data
  private static class TestTokenData {
    String audience;
    String issuer;
    Date expiration;
    String expectedErrorMessage;
    java.security.PrivateKey privateKey;

    TestTokenData(String audience, String issuer, Date expiration, String expectedErrorMessage)
        throws Exception {
      this.audience = audience;
      this.issuer = issuer;
      this.expiration = expiration;
      this.expectedErrorMessage = expectedErrorMessage;
      // Generate a new key pair for each test case so that signature verification is done with our
      // cached key intentionally failing
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      generator.initialize(2048);
      KeyPair kp = generator.generateKeyPair();
      this.privateKey = kp.getPrivate();
    }
  }

  // Placeholder string for CSV Source which cannot reference instance data.
  private static final String FAKER_PLACEHOLDER = "FAKER_PLACEHOLDER";
}
