package pl.edu.zut.app.parking.notifications.exceptions;

import java.io.IOException;

public class PushNotificationProcessException extends NotificationServiceException {
    public PushNotificationProcessException(String message, IOException e) {
        super(message);
        e.printStackTrace();
    }
}
