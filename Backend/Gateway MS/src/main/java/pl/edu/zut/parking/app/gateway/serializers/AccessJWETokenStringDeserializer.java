package pl.edu.zut.parking.app.gateway.serializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.zut.parking.app.gateway.dto.Token;

import java.text.ParseException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccessJWETokenStringDeserializer implements TokenDeserializer {

    private final JWSVerifier verifier;

    /**
     * Deserializes a token from a string representation
     *
     * @param token the token to be deserialized
     * @return Token object or null if the token is invalid
     */
    @Override
    public Token apply(String token) {
        try {
            var encryptedJWT = SignedJWT.parse(token);
            if (encryptedJWT.verify(this.verifier)) {
                return new Token(
                        UUID.fromString(encryptedJWT.getHeader().getKeyID()),
                        encryptedJWT.getJWTClaimsSet().getSubject(),
                        encryptedJWT.getJWTClaimsSet().getIssuer(),
                        encryptedJWT.getJWTClaimsSet().getStringListClaim("authorities"),
                        encryptedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                        encryptedJWT.getJWTClaimsSet().getExpirationTime().toInstant()
                );
            }
            return null;
        } catch (ParseException | JOSEException | IllegalArgumentException e) {
            log.error("Error parsing JWT", e);
            return null;
        }
    }
}
