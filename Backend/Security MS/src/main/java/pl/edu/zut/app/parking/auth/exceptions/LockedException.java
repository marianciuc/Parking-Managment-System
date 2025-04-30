package pl.edu.zut.app.parking.auth.exceptions;

public class LockedException extends SecurityServiceException {
    public LockedException(String accountIsLocked) {
        super(accountIsLocked);
    }
}
