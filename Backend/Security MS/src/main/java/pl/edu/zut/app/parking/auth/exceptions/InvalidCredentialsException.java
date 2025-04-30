package pl.edu.zut.app.parking.auth.exceptions;

public class InvalidCredentialsException extends SecurityServiceException{
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
