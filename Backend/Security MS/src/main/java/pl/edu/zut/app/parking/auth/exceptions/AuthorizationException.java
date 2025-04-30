package pl.edu.zut.app.parking.auth.exceptions;

public class AuthorizationException extends SecurityServiceException{
    public AuthorizationException(String message) {
        super(message);
    }
}
