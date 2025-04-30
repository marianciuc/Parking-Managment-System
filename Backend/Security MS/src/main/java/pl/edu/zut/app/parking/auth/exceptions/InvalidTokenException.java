package pl.edu.zut.app.parking.auth.exceptions;

public class InvalidTokenException extends SecurityServiceException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
