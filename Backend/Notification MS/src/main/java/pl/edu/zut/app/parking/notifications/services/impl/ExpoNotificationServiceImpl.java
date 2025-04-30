package pl.edu.zut.app.parking.notifications.services.impl;

import com.niamedtech.expo.exposerversdk.ExpoPushNotificationClient;
import com.niamedtech.expo.exposerversdk.request.PushNotification;
import com.niamedtech.expo.exposerversdk.response.Status;
import com.niamedtech.expo.exposerversdk.response.TicketResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.notifications.entities.NotificationConsumer;
import pl.edu.zut.app.parking.notifications.exceptions.DeviceNotFoundException;
import pl.edu.zut.app.parking.notifications.exceptions.PushNotificationProcessException;
import pl.edu.zut.app.parking.notifications.repositories.NotificationConsumersRepository;
import pl.edu.zut.app.parking.notifications.services.ExpoPushNotificationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpoNotificationServiceImpl implements ExpoPushNotificationService {

  private final NotificationConsumersRepository repository;

  /**
   * Sends a push notification to a specific user.
   *
   * @param userId the unique identifier of the user to whom the notification will be sent
   * @param title the title of the notification
   * @param message the message content of the notification
   * @throws PushNotificationProcessException if an error occurs during the push notification
   *     process
   */
  @Override
  public void sendPushNotification(UUID userId, String title, String message)
      throws PushNotificationProcessException {
    List<String> consumers =
        repository.findAllByUserId(userId).stream()
            .map(this::getExpoPushNotificationToken)
            .toList();
    if (consumers.isEmpty()) {
      log.warn("No consumers found for user {}", userId);
      return;
    }
    log.info("Sending push notification to user {} with consumers {}", userId, consumers);

    CloseableHttpClient httpClient = HttpClients.createDefault();
    ExpoPushNotificationClient client =
        ExpoPushNotificationClient.builder().setHttpClient(httpClient).build();

    PushNotification pushNotification = new PushNotification();
    pushNotification.setTo(consumers);
    pushNotification.setTitle(title);
    pushNotification.setBody(message);

    try {
      List<TicketResponse.Ticket> response =
          client.sendPushNotifications(List.of(pushNotification));

      for (TicketResponse.Ticket ticket : response) {
        log.info("Push notification sent to user {} with status {}", userId, ticket.getStatus());
        if (ticket.getStatus().equals(Status.ERROR)) {
          log.error(
              "Error sending push notification to user {}\n, Data: {}\n, Message: {}",
              userId,
              ticket.getDetails(),
              ticket.getMessage());
        }
      }
    } catch (IOException e) {
      log.error("Failed to send push notification to user {}", userId, e);
      throw new PushNotificationProcessException("Error sending push notification", e);
    }
  }

  private String getExpoPushNotificationToken(NotificationConsumer consumer) {
    return String.format("ExponentPushToken[%s]", consumer.getExpoPushNotificationToken());
  }

  /**
   * Removes the device token associated with the specified user.
   *
   * @param userId the unique identifier of the user whose device token will be removed
   * @param deviceToken the token of the device to be associated with the specified user
   * @throws DeviceNotFoundException if the specified user does not have a device token
   */
  @Override
  public void removeDeviceToken(UUID userId, String deviceToken) throws DeviceNotFoundException {
    NotificationConsumer notificationConsumer =
        repository.findByUserIdAndExpoPushNotificationToken(userId, deviceToken);
    if (notificationConsumer == null) {
      throw new DeviceNotFoundException("Device token not found for user " + userId);
    }
    repository.delete(notificationConsumer);
  }

  /**
   * Adds a new device token for the specified user. The device token is used for sending push
   * notifications through Expo services.
   *
   * @param userId the unique identifier of the user to whom the device token belongs
   * @param deviceToken the token of the device to be associated with the specified user
   * @throws DeviceNotFoundException if the specified user's device cannot be found
   */
  @Override
  public void addDeviceToken(UUID userId, String deviceToken) throws DeviceNotFoundException {
    NotificationConsumer notificationConsumer =
        NotificationConsumer.builder()
            .userId(userId)
            .expoPushNotificationToken(deviceToken)
            .isSubscribed(true)
            .build();
    repository.save(notificationConsumer);
  }

  /**
   * Sends a push notification to all users.
   *
   * @param title the title of the notification
   * @param message the message content of the notification
   */
  @Override
  public void sendPushNotificationToAll(String title, String message) {
    for (UUID userId : repository.findAllWhereUserIdIsUniq()) {
      try {
        sendPushNotification(userId, title, message);
      } catch (PushNotificationProcessException e) {
        log.error(e.getMessage(), e);
      }
    }
  }
}
