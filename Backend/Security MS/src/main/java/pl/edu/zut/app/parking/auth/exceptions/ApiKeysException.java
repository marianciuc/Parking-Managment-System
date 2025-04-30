package pl.edu.zut.app.parking.auth.exceptions;

public abstract class ApiKeysException extends SecurityServiceException {
    public ApiKeysException(String message) {
        super(message);
    }
}
