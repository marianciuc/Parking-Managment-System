package pl.edu.zut.parking.app.gateway.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pl.edu.zut.parking.app.gateway.services.TokenBlackListService;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JWTBlackListServiceImpl implements TokenBlackListService {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Blocks a specified token for a given duration.
     *
     * @param token                    the token to be blocked
     * @param expirationTimeInMilliseconds the duration (in milliseconds) for which the token remains blocked
     */
    @Override
    public void blockToken(String token, int expirationTimeInMilliseconds) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty.");
        }
        if (expirationTimeInMilliseconds <= 0) {
            throw new IllegalArgumentException("Expiration time must be greater than zero.");
        }

        String key = generateBlackListKey(token);
        redisTemplate.opsForValue().set(key, "", expirationTimeInMilliseconds, TimeUnit.MILLISECONDS);
    }

    /**
     * Checks if the specified token is currently blocked.
     *
     * @param token the token to be checked
     * @return {@code true} if the token is blocked; {@code false} otherwise
     */
    @Override
    public boolean isTokenBlocked(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String key = generateBlackListKey(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Generates a Redis key using the blacklist token prefix.
     *
     * @param token the token for which the key is generated
     * @return the generated Redis key
     */
    private String generateBlackListKey(String token) {
        return BLACKLIST_PREFIX + token;
    }
}
