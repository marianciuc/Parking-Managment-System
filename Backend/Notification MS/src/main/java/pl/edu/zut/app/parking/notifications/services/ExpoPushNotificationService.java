package pl.edu.zut.app.parking.notifications.services;

import java.util.UUID;
import pl.edu.zut.app.parking.notifications.exceptions.DeviceNotFoundException;
import pl.edu.zut.app.parking.notifications.exceptions.PushNotificationProcessException;

/**
 * The ExpoPushNotificationService interface defines methods for managing and sending push
 * notifications using Expo services to specific users or all users. This service includes features
 * for handling device tokens.
 */
public interface ExpoPushNotificationService {

  /**
   * Sends a push notification to a specific user.
   *
   * @param userId the unique identifier of the user to whom the notification will be sent
   * @param title the title of the notification
   * @param message the message content of the notification
   * @throws PushNotificationProcessException if an error occurs during the push notification
   *     process
   */
  void sendPushNotification(UUID userId, String title, String message)
      throws PushNotificationProcessException;

  /**
   * Removes the device token associated with the specified user.
   *
   * @param userId the unique identifier of the user whose device token will be removed
   * @param deviceToken the token of the device to be associated with the specified user
   * @throws DeviceNotFoundException if the specified user does not have a device token
   */
  void removeDeviceToken(UUID userId, String deviceToken) throws DeviceNotFoundException;

  /**
   * Adds a new device token for the specified user. The device token is used for sending push
   * notifications through Expo services.
   *
   * @param userId the unique identifier of the user to whom the device token belongs
   * @param deviceToken the token of the device to be associated with the specified user
   * @throws DeviceNotFoundException if the specified user's device cannot be found
   */
  void addDeviceToken(UUID userId, String deviceToken) throws DeviceNotFoundException;

  /**
   * Sends a push notification to all users.
   *
   * @param title the title of the notification
   * @param message the message content of the notification
   * @throws PushNotificationProcessException if an error occurs during the push notification
   *     process
   */
  void sendPushNotificationToAll(String title, String message)
      throws PushNotificationProcessException;
}
