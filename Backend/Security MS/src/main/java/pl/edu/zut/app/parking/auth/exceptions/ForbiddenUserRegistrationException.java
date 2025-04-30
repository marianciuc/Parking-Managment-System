package pl.edu.zut.app.parking.auth.exceptions;

public class ForbiddenUserRegistrationException extends RuntimeException {
    public ForbiddenUserRegistrationException(String message) {
        super(message);
    }
}
