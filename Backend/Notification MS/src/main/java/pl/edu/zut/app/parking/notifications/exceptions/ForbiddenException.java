package pl.edu.zut.app.parking.notifications.exceptions;

public class ForbiddenException extends NotificationServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
