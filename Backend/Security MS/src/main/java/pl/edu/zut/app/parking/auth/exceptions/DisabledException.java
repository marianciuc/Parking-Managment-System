package pl.edu.zut.app.parking.auth.exceptions;

public class DisabledException extends SecurityServiceException {
    public DisabledException(String accountIsDisabled) {
        super(accountIsDisabled);
    }
}
