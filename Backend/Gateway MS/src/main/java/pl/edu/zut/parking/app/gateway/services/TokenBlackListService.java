package pl.edu.zut.parking.app.gateway.services;

/**
 * Service for managing the black list of tokens.
 */
public interface TokenBlackListService {

    /**
     * Blocks a specified token for a given duration.
     *
     * @param token the token to be blocked
     * @param expirationTimeInMilliseconds the duration in milliseconds for which the token should be blocked
     */
    void blockToken(String token, int expirationTimeInMilliseconds);

    /**
     * Checks if the specified token is currently blocked.
     *
     * @param token the token to be checked
     * @return true if the token is blocked; false otherwise
     */
    boolean isTokenBlocked(String token);
}
