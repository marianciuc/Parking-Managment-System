package pl.edu.zut.app.parking.notifications.services;

import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.notifications.dto.NotificationDto;
import pl.edu.zut.app.parking.notifications.enums.NotificationTopic;

import java.util.UUID;

public interface NotificationService {
    void sendPersonalNotification(UUID userId, String title, String message);
    void sendPersonalNotification(UUID userId, Object object, NotificationTopic notificationTopic);
    void sendNotificationToAll(String title, String message);
    Page<NotificationDto> getNotifications(UUID userId, int page, int size);
    NotificationDto markAsRead(UUID notificationId);
    void deleteNotification(UUID notificationId);
}
