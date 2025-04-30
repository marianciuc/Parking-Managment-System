package pl.edu.zut.app.parking.auth.services;

import com.nimbusds.jwt.JWTClaimsSet;
import pl.edu.zut.app.parking.auth.exceptions.TokenValidationException;

/**
 * GoogleJWTValidator is an interface for validating Google ID tokens.
 * It verifies the signature and claims of a provided Google ID token.
 */
public interface GoogleJWTValidator {

    /**
     * Verifies the provided Google ID token by checking its signature and claims.
     * @param idToken the ID token to validate
     * @return the verified claims from the ID token
     * @throws TokenValidationException if the validation fails
     */
    JWTClaimsSet verifyGoogleToken(String idToken);
}
