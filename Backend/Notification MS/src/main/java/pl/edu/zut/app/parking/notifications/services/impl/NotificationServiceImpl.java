package pl.edu.zut.app.parking.notifications.services.impl;

import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.notifications.builder.NotificationContentFactory;
import pl.edu.zut.app.parking.notifications.dto.NotificationContent;
import pl.edu.zut.app.parking.notifications.dto.NotificationDto;
import pl.edu.zut.app.parking.notifications.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.notifications.entities.Notification;
import pl.edu.zut.app.parking.notifications.enums.NotificationTopic;
import pl.edu.zut.app.parking.notifications.exceptions.NotificationNotFoundException;
import pl.edu.zut.app.parking.notifications.repositories.NotificationRepository;
import pl.edu.zut.app.parking.notifications.services.ExpoPushNotificationService;
import pl.edu.zut.app.parking.notifications.services.NotificationService;
import pl.edu.zut.app.parking.notifications.services.WebSocketService;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final WebSocketService webSocketService;
  private final ExpoPushNotificationService expoPushNotificationService;
  private final NotificationContentFactory notificationContentFactory;

  @Override
  public void sendPersonalNotification(UUID userId, String title, String message) {
    log.info("Sending personal notification to user {}", userId);
    saveNotificationToDatabase(prepareNotification(title, message, userId));
    expoPushNotificationService.sendPushNotification(userId, title, message);
    log.info("Personal notification sent to user {}", userId);
  }

  @Override
  public void sendPersonalNotification(
      UUID userId, Object object, NotificationTopic notificationTopic) {
    log.info("Sending personal notification to user {} with topic {}", userId, notificationTopic);
    NotificationContent notificationContent =
        notificationContentFactory.createContent(notificationTopic, object);

    notificationRepository.save(
        prepareNotification(notificationContent.title(), notificationContent.content(), userId));

    expoPushNotificationService.sendPushNotification(
        userId, notificationContent.title(), notificationContent.content());
    log.info("Personal notification sent to user {}", userId);
  }

  @Override
  public void sendNotificationToAll(String title, String message) {
    log.info("Sending global notification to all users");
    Notification notification =
        saveNotificationToDatabase(prepareNotification(title, message, null));

    expoPushNotificationService.sendPushNotificationToAll(
        notification.getTitle(), notification.getMessage());
    log.info("Global notification sent to all users");
  }

  @Override
  public Page<NotificationDto> getNotifications(UUID userId, int page, int size) {
    log.info("Getting notifications for user {} with page {} and size {}", userId, page, size);
    if (page < 0 || size <= 0) {
      throw new IllegalArgumentException("Page number and size must be positive values");
    }

    return notificationRepository
        .findAllByReceiverIdAndRecordStatus(
            userId, AbstractBaseEntity.RecordStatus.ACTIVE, PageRequest.of(page, size,
                        Sort.by("creationDate").descending()))
        .map(NotificationDto::fromEntity);
  }

  @Override
  @Transactional
  public NotificationDto markAsRead(UUID notificationId) {
    log.info("Marking notification {} as read", notificationId);
    Notification notification =
        notificationRepository
            .findByIdAndRecordStatus(notificationId, AbstractBaseEntity.RecordStatus.ACTIVE)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

    notification.setRead(true);
    return NotificationDto.fromEntity(notificationRepository.save(notification));
  }

  @Override
  public void deleteNotification(UUID notificationId) {
    log.info("Deleting notification {}", notificationId);
    Notification notification =
        notificationRepository
            .findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

    notification.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
    notificationRepository.save(notification);
    log.info("Notification {} deleted", notificationId);
  }

  private Notification saveNotificationToDatabase(Notification notification) {
    log.info("Saving notification to database: {}", notification);
    return notificationRepository.save(notification);
  }

  private Notification prepareNotification(String title, String message, UUID receiverId) {
    log.info("Preparing notification with title {} and message {}", title, message);
    return Notification.builder()
        .message(message)
        .title(title)
        .receiverId(receiverId)
        .isRead(false)
        .recordStatus(AbstractBaseEntity.RecordStatus.ACTIVE)
        .build();
  }
}
