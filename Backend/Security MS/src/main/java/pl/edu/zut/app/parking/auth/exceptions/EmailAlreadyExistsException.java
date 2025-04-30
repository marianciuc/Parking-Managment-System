package pl.edu.zut.app.parking.auth.exceptions;

public class EmailAlreadyExistsException extends SecurityServiceException {
    public EmailAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
    }
}