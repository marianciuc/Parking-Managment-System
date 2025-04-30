package pl.edu.zut.app.parking.auth.exceptions;

public abstract class JWTException extends SecurityServiceException {
    public JWTException(String message, Throwable cause) {
        super(message);
    }
}
