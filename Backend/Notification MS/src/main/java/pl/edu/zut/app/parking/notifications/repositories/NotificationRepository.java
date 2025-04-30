package pl.edu.zut.app.parking.notifications.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.notifications.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.notifications.entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("select n from Notification n where n.receiverId = ?1")
    Page<Notification> findAllByReceiverId(UUID receiverId, Pageable pageable);

    @Query("select n from Notification n where n.id = ?1 and n.recordStatus = ?2")
    Optional<Notification> findByIdAndRecordStatus(UUID id, AbstractBaseEntity.RecordStatus recordStatus);

    @Query("select n from Notification n where n.receiverId = ?1 and n.recordStatus = ?2")
    Page<Notification> findAllByReceiverIdAndRecordStatus(UUID userId, AbstractBaseEntity.RecordStatus recordStatus,
                                                          Pageable pageable);
}
