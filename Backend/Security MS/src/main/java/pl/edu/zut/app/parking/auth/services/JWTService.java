package pl.edu.zut.app.parking.auth.services;

import org.springframework.security.core.Authentication;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.entities.User;

/**
 * JwtService is an interface responsible for managing JWT tokens.
 */
public interface JWTService {

    /**
     * Generates a new access token based on the provided refresh token.
     *
     * @param refreshAuthToken the refresh token used to create a new access token
     * @return a new access token created from the provided refresh token
     */
    AuthToken createAccessToken(AuthToken refreshAuthToken);

    /**
     * Generates a pair of authentication tokens (access token and refresh token)
     * for the given user.
     *
     * @param user the user entity for whom the token pair is to be generated
     * @return a {@code TokenPair} containing the generated access token,
     *         its expiry time, the refresh token, and its expiry time
     */
    AuthenticationTokens generateAuthenticationTokens(User user);

    /**
     * Creates a new refresh token for the provided user principal.
     *
     * @return a new refresh token for the provided user principal
     */
    AuthToken createRefreshToken(Authentication authentication);

    /**
     * Retrieves the principal user from the provided token.
     *
     * @param token the JWT token used to identify and fetch the associated user
     * @return the {@code User} object corresponding to the given token
     */
    User getPrincipal(String token);

    /**
     * Retrieves the public key used for cryptographic operations such as
     * verifying JSON Web Tokens (JWTs).
     *
     * @return the public key as a {@code String}
     */
    String getPublicKey();

    /**
     * Deserializes a given JWT access token string into a {@code Token} object.
     *
     * @param token the serialized JWT access token as a string
     * @return the corresponding {@code Token} object, or {@code null} if the token is invalid or deserialization fails
     */
    AuthToken accessTokenStringDeserialize(String token);

    /**
     * Serializes the given access token into its string representation.
     *
     * @param authToken the access token to be serialized; must contain valid token information such as ID, user ID, roles, etc.
     * @return the serialized string representation of the access token
     */
    String serializeAccessToken(AuthToken authToken);

    /**
     * Serializes the given refresh token into its string representation.
     *
     * @param authToken the refresh token to be serialized; must contain valid token information such as ID, user ID, roles, etc.
     * @return the serialized string representation of the refresh token
     */
    String serializeRefreshToken(AuthToken authToken);

    AuthenticationTokens generateAuthenticationTokens(Authentication authentication);

    AuthenticationTokens refreshAuthenticationTokens(Authentication authentication);

    AuthToken refreshTokenStringDeserialize(String token);
}
