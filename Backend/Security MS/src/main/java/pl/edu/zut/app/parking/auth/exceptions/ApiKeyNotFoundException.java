package pl.edu.zut.app.parking.auth.exceptions;

public class ApiKeyNotFoundException extends ApiKeysException {
    public ApiKeyNotFoundException(String message) {
        super(message);
    }
}
