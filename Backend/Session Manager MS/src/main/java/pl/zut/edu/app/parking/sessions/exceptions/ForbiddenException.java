package pl.zut.edu.app.parking.sessions.exceptions;

public class ForbiddenException extends SecurityException {

    public ForbiddenException(String message) {
        super(message);
    }
}
