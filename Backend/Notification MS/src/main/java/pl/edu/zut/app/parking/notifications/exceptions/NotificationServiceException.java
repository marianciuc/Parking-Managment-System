package pl.edu.zut.app.parking.notifications.exceptions;

public abstract class NotificationServiceException extends RuntimeException {
    public NotificationServiceException(String message) {
        super(message);
    }
}
