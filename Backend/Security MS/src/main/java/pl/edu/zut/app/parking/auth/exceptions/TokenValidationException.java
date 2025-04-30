package pl.edu.zut.app.parking.auth.exceptions;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message) {
        super(message);
    }
    public TokenValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
