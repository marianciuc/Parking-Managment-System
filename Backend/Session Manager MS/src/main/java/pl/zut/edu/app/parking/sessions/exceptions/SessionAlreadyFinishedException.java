package pl.zut.edu.app.parking.sessions.exceptions;

public class SessionAlreadyFinishedException extends SessionServiceException {
    public SessionAlreadyFinishedException(String message) {
        super(message);
    }
}
