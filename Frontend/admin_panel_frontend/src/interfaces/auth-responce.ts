/**
 * Represents an authentication response received after a successful authentication attempt.
 * @param accessToken the JWT access token
 * @param refreshToken the JWT encoded refresh token
 * @param refreshTokenExpiry time of refresh token expiration
 * @param accessTokenExpiry time of access token expiration
 */
export default interface AuthResponse {
    accessToken: string;
    refreshToken: string;
    refreshTokenExpiry: string;
    accessTokenExpiry: string;
}