package pl.edu.zut.app.parking.auth.utils;

/**
 * A utility class that contains constants related to the OAuth2 authorization and authentication process.
 * This class provides pre-defined values for commonly used configurations and parameters in the OAuth2 flow.
 * These constants are intended to standardize and simplify the usage of OAuth2 integrations.
 */
public class OAuth2Constants {

    public static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String DEFAULT_SCOPE = "openid email profile";

    private OAuth2Constants() {
        throw new IllegalStateException("Utility class");
    }
}
