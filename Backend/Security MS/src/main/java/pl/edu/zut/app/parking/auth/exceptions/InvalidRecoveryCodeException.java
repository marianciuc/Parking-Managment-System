package pl.edu.zut.app.parking.auth.exceptions;

public class InvalidRecoveryCodeException extends RuntimeException {
    public InvalidRecoveryCodeException(String message) {
        super(message);
    }
}
