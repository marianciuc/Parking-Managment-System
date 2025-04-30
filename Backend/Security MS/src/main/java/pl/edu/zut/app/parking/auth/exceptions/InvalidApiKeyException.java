package pl.edu.zut.app.parking.auth.exceptions;

public class InvalidApiKeyException extends ApiKeysException {
    public InvalidApiKeyException(String message) {
        super(message);
    }
}
