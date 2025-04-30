package pl.zut.edu.app.parking.sessions.exceptions;

public class BusinessException extends SessionServiceException {
    public BusinessException(String message) {
        super(message);
    }
}
