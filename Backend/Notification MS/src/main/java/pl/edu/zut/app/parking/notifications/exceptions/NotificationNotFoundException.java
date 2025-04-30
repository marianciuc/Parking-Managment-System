package pl.edu.zut.app.parking.notifications.exceptions;

import java.util.UUID;

public class NotificationNotFoundException extends NotificationServiceException {
    public NotificationNotFoundException(UUID uuid) {
        super("Notification with id " + uuid + " not found");
    }
}
