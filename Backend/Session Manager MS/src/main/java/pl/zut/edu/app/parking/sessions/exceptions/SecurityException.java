package pl.zut.edu.app.parking.sessions.exceptions;

public abstract class SecurityException extends SessionServiceException {
    public SecurityException(String message) {
        super(message);
    }
}
