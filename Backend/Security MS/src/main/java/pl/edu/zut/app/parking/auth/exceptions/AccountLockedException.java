package pl.edu.zut.app.parking.auth.exceptions;

public class AccountLockedException extends SecurityServiceException {
    public AccountLockedException(String message) {
        super(message);
    }
}
