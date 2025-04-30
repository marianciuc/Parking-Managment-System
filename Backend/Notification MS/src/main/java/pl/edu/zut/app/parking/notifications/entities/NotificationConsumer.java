package pl.edu.zut.app.parking.notifications.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "notification_consumers")
public class NotificationConsumer extends AbstractBaseEntity {

  @Column(name = "expo_push_notification_token")
  private String expoPushNotificationToken;

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "is_subscribed")
  private boolean isSubscribed;
}
