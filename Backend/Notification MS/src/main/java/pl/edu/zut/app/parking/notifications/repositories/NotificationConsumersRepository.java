package pl.edu.zut.app.parking.notifications.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.notifications.entities.NotificationConsumer;

public interface NotificationConsumersRepository extends JpaRepository<NotificationConsumer, UUID> {

  /**
   * Retrieves a list of NotificationConsumer entities associated with a specific user ID.
   *
   * @param userId the unique identifier of the user whose notification consumers are to be
   *     retrieved
   * @return a list of NotificationConsumer entities linked to the specified user ID
   */
  @Query("select n from NotificationConsumer n where n.userId = ?1")
  List<NotificationConsumer> findAllByUserId(UUID userId);

  @Query("select n from NotificationConsumer n where n.userId = ?1 and n.expoPushNotificationToken = ?2")
  NotificationConsumer findByUserIdAndExpoPushNotificationToken(UUID userId, String expoPushNotificationToken);

  @Query("select DISTINCT n.userId from NotificationConsumer n")
  List<UUID> findAllWhereUserIdIsUniq();
}
