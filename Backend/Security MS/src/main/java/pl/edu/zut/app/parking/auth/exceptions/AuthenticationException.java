package pl.edu.zut.app.parking.auth.exceptions;

public class AuthenticationException extends SecurityServiceException {
    public AuthenticationException(String message) {
        super(message);
    }
}
