package pl.edu.zut.app.parking.notifications.builder;

import pl.edu.zut.app.parking.notifications.dto.NotificationContent;
import pl.edu.zut.app.parking.notifications.kafka.messages.SessionStartedMessage;

public class SessionStartedNotificationBuilder implements NotificationContentBuilder {

    /**
     * Builds a NotificationContent instance based on the provided data.
     *
     * @param data the input data used to construct the NotificationContent
     * @return a NotificationContent instance containing the generated title and content
     */
    @Override
    public NotificationContent build(Object data) {
        if (data instanceof SessionStartedMessage) {
            return new NotificationContent(
                    "New session started",
                    String.format("New session was started for %s", ((SessionStartedMessage) data).plateNumber())
            );
        } else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }
}
