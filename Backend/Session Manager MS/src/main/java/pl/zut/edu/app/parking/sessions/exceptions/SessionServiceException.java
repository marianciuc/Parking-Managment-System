package pl.zut.edu.app.parking.sessions.exceptions;

public abstract class SessionServiceException extends RuntimeException {
    public SessionServiceException(String message) {
        super(message);
    }
}
