package pl.edu.zut.app.parking.notifications.dto;

import pl.edu.zut.app.parking.notifications.entities.Notification;

import java.util.UUID;

public record NotificationDto(
        UUID id,
        UUID receiverId,
        String title,
        String message,
        String creationDate,
        String modificationDate
) {
    public static NotificationDto fromEntity(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getReceiverId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getCreationDate().toString(),
                notification.getModificationDate().toString()
        );
    }
}
