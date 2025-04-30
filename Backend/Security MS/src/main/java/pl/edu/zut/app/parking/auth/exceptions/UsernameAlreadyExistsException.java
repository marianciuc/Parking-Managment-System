package pl.edu.zut.app.parking.auth.exceptions;

public class UsernameAlreadyExistsException extends SecurityServiceException {
    public UsernameAlreadyExistsException(String username) {
        super("User with username " + username + " already exists");
    }
}