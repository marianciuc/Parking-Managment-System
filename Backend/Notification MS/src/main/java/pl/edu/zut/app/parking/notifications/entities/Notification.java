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
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Table(name = "notifications")
public class Notification extends AbstractBaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "receiver_id")
    private UUID receiverId;

    @Column(name = "is_read")
    private boolean isRead;
}
