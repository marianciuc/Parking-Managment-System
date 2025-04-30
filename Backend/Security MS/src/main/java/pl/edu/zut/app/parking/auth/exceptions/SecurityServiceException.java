package pl.edu.zut.app.parking.auth.exceptions;

public abstract class SecurityServiceException extends RuntimeException {
    public SecurityServiceException(String message) {
        super(message);
    }

    public SecurityServiceException(String message, Throwable cause) {
    }
}
