package pl.edu.zut.app.parking.notifications.builder;

import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.notifications.dto.NotificationContent;
import pl.edu.zut.app.parking.notifications.enums.NotificationTopic;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationContentFactory {

  private final Map<NotificationTopic, NotificationContentBuilder> contentBuilders = new HashMap<>();

  public NotificationContentFactory() {
    contentBuilders.put(
        NotificationTopic.SESSION_STARTED, new SessionStartedNotificationBuilder());
    contentBuilders.put(NotificationTopic.SESSION_ENDED, new SessionEndedNotificationBuilder());
  }

  public NotificationContent createContent(NotificationTopic topic, Object data) {
    NotificationContentBuilder builder = contentBuilders.get(topic);

    if (builder == null) {
      throw new IllegalArgumentException("The invalid topic: " + topic);
    }

    return builder.build(data);
  }
}
