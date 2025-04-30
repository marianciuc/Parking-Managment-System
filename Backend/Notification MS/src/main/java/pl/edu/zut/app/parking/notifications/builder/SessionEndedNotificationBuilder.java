package pl.edu.zut.app.parking.notifications.builder;

import java.time.Duration;
import pl.edu.zut.app.parking.notifications.dto.NotificationContent;
import pl.edu.zut.app.parking.notifications.kafka.messages.SessionEndedMessage;

public class SessionEndedNotificationBuilder implements NotificationContentBuilder {

  private static final String TITLE = "Session ended";
  private static final String CONTENT_TEMPLATE =
      "The parking session ended for %s. Duration: %d minutes";

  /**
   * Builds a NotificationContent instance based on the provided data.
   *
   * @param data the input data used to construct the NotificationContent
   * @return a NotificationContent instance containing the generated title and content
   */
  @Override
  public NotificationContent build(Object data) {
    if (data instanceof SessionEndedMessage message) {
      long durationMinutes = Duration.between(message.timeStarted(), message.endTime()).toMinutes();

      return new NotificationContent(
          TITLE, String.format(CONTENT_TEMPLATE, message.plateNumber(), durationMinutes));
    } else {
      throw new IllegalArgumentException("Unsupported data type");
    }
  }
}
