package pl.edu.zut.app.parking.auth.exceptions;

public class ExpiredTokenException extends SecurityServiceException{
    public ExpiredTokenException(String message) {
        super(message);
    }
}
