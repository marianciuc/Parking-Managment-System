package pl.edu.zut.app.parking.auth.serializers;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.exceptions.TokenEncryptionException;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class RefreshJWETokenStringSerializer implements TokenSerializer {

    private final JWEEncrypter jweEncrypter;
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    /**
     * Applies the token serialization process to produce a JWE-encoded representation of the given token.
     *
     * @param authToken the token to be serialized; contains claims and metadata for the JWE token.
     * @return a string representing the JWE-encoded token.
     * @throws TokenEncryptionException if the token encryption process fails.
     */
    @Override
    public String apply(AuthToken authToken) {
        var jwtHeader =  new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                .keyID(UUID.randomUUID().toString())
                .contentType("JWT")
                .build();

        var jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(authToken.subject())
                .issueTime(Date.from(authToken.createdAt()))
                .expirationTime(Date.from(authToken.expiresAt()))
                .issuer(authToken.issuer())
                .claim("authorities", authToken.roles())
                .build();

        JWEObject jweObject = new JWEObject(jwtHeader, new Payload(jwtClaimsSet.toJSONObject()));
        try {
            jweObject.encrypt(this.jweEncrypter);
            return jweObject.serialize();
        } catch (JOSEException e) {
            log.error("Error encrypting JWT", e);
            throw new TokenEncryptionException("Failed to encrypt JWT", e);
        }
    }

    public RefreshJWETokenStringSerializer encryptionMethod(EncryptionMethod encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
        return this;
    }

    public RefreshJWETokenStringSerializer jweAlgorithm(JWEAlgorithm jweAlgorithm) {
        this.jweAlgorithm = jweAlgorithm;
        return this;
    }

}
