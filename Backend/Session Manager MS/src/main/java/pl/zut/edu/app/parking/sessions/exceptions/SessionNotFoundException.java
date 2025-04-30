package pl.zut.edu.app.parking.sessions.exceptions;

public class SessionNotFoundException extends SessionServiceException {
    public SessionNotFoundException(String message) {
        super(message);
    }
}
