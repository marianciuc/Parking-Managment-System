package pl.edu.zut.app.parking.auth.dto.common;

/**
 * Record representing a pair of tokens in the authentication system.
 *
 * @param accessToken        the access token
 * @param accessTokenExpiry  the expiry time of the access token
 * @param refreshToken       the refresh token
 * @param refreshTokenExpiry the expiry time of the refresh token
 */
public record AuthenticationTokens(
        String accessToken,
        String accessTokenExpiry,
        String refreshToken,
        String refreshTokenExpiry
) {
}