package pl.edu.zut.app.parking.auth.exceptions;

public abstract class UserRegistrationException extends SecurityServiceException {
    public UserRegistrationException(String message) {
        super(message);
    }
}
