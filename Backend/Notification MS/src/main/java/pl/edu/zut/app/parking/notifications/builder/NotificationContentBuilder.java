package pl.edu.zut.app.parking.notifications.builder;

import pl.edu.zut.app.parking.notifications.dto.NotificationContent;

/**
 * Interface responsible for building instances of {@code NotificationContent}.
 * Implementations of this interface are expected to generate a {@code NotificationContent}
 * object based on the provided input data.
 */
public interface NotificationContentBuilder {

    /**
     * Builds a NotificationContent instance based on the provided data.
     *
     * @param data the input data used to construct the NotificationContent
     * @return a NotificationContent instance containing the generated title and content
     */
    NotificationContent build(Object data);
}
