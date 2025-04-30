package pl.edu.zut.app.parking.auth.services;

import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;

/**
 * OAuth2Service is an interface responsible for handling operations related to
 * OAuth 2.0 authentication for different providers, such as Google and Apple.
 */
public interface OAuth2Service {

    /**
     * Retrieves the URL to initiate the Google authentication process.
     *
     * @return the URL as a {@code String} for Google authentication
     */
    String getGoogleAuthenticatorUrl();


    /**
     * Retrieves the URL to initiate the Apple authentication process.
     *
     * @return the URL as a {@code String} for Apple authentication
     */
    String getAppleAuthenticatorUrl();


    /**
     * Processes the authentication response from Google and generates a pair of authentication tokens.
     *
     * @param code the authorization code received from Google after a successful authentication
     * @return an {@link AuthenticationTokens} object containing the access token,
     *         refresh token, and their respective expiry times
     * @throws pl.edu.zut.app.parking.auth.exceptions.ForbiddenException if a user doesn't have permissions to login
     * within external authentication provider or necessary permissions
     */
    AuthenticationTokens handleGoogleAuthenticatorResponse(String code);

    /**
     * Processes the authentication response from Apple's OAuth 2.0 service and
     * generates a set of authentication tokens.
     *
     * @param code the authorization code received from Apple after a successful authentication
     * @return an {@link AuthenticationTokens} object containing the access token,
     *         refresh token, and their respective expiry information
     */
    AuthenticationTokens handleAppleAuthenticatorResponse(String code);
}
