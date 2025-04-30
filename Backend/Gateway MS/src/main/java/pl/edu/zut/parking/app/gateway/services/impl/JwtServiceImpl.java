package pl.edu.zut.parking.app.gateway.services.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.edu.zut.parking.app.gateway.dto.Token;
import pl.edu.zut.parking.app.gateway.serializers.TokenDeserializer;
import pl.edu.zut.parking.app.gateway.services.JwtService;

import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private final TokenDeserializer tokenDeserializer;

    public JwtServiceImpl(@Lazy TokenDeserializer tokenDeserializer) {
        this.tokenDeserializer = tokenDeserializer;
    }

    @Override
    public Optional<String> extractBearerToken(String header) {
        if (isBearerToken(header)) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }


    @Override
    public Token parseToken(String token) throws RuntimeException {
        try {
            return tokenDeserializer.apply(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Checks if the provided header contains a Bearer token.
     *
     * @param header the value of the HTTP Authorization header
     * @return true if the header contains a Bearer token; otherwise, false
     */
    private boolean isBearerToken(String header) {
        return header != null && header.startsWith("Bearer ");
    }
}