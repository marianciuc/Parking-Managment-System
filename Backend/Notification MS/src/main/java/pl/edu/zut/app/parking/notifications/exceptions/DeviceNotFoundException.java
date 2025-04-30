package pl.edu.zut.app.parking.notifications.exceptions;

import java.util.UUID;

public class DeviceNotFoundException extends NotificationServiceException {
  public DeviceNotFoundException(String message) {
    super(message);
  }
}
