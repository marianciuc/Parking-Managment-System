package pl.edu.zut.app.parking.auth.exceptions;

public class AccountExpiredException extends SecurityServiceException{
    public AccountExpiredException(String message) {
        super(message);
    }

}
