package pl.edu.zut.app.parking.auth.exceptions;

public class UnauthenticatedUserRegistrationException extends UserRegistrationException {
    public UnauthenticatedUserRegistrationException(String userNotAuthenticated) {
        super(userNotAuthenticated);
    }
}
