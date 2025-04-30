package pl.edu.zut.app.parking.auth.serializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.exceptions.TokenSigningException;

import java.util.Date;

@Slf4j
public class AccessJWSTokenStringSerializer implements TokenSerializer {

    private final JWSSigner signer;
    private JWSAlgorithm algorithm = JWSAlgorithm.HS256;

    public AccessJWSTokenStringSerializer(JWSSigner signer) {
        this.signer = signer;
    }

    /**
     * Applies the token serialization process to produce a JWS-encoded representation of the given token.
     * @param authToken the token to be serialized;
     * @return a string representing the JWS-encoded token.
     * @throws TokenSigningException if the token signing process fails.
     */
    @Override
    public String apply(AuthToken authToken) {
        var jwtHeader = new JWSHeader.Builder(algorithm)
                .keyID(authToken.tokenId().toString())
                .build();
        var jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(authToken.tokenId().toString())
                .subject(authToken.subject())
                .issueTime(Date.from(authToken.createdAt()))
                .expirationTime(Date.from(authToken.expiresAt()))
                .issuer(authToken.issuer())
                .claim("authorities", authToken.roles().stream().map(s -> s.replace("GRAND::", "")).toList())
                .build();
        var signedJWT = new SignedJWT(jwtHeader, jwtClaimsSet);
        try {
            signedJWT.sign(this.signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWT", e);
            throw new TokenSigningException("Failed to sign JWT", e);
        }
    }

    public void setAlgorithm(JWSAlgorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }
        this.algorithm = algorithm;
    }

    public AccessJWSTokenStringSerializer algorithm(JWSAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }
}
