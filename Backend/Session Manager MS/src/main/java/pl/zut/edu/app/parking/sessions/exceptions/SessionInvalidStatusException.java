package pl.zut.edu.app.parking.sessions.exceptions;

public class SessionInvalidStatusException extends SessionServiceException {
    public SessionInvalidStatusException(String message) {
        super(message);
    }
}
