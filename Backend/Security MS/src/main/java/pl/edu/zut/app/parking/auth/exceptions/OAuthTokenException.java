package pl.edu.zut.app.parking.auth.exceptions;

public class OAuthTokenException extends RuntimeException {
    public OAuthTokenException(String message) {
        super(message);
    }
}
