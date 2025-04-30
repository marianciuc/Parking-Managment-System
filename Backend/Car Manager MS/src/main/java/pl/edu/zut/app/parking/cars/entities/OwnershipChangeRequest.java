package pl.edu.zut.app.parking.cars.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "ownership_change_requests")
public class OwnershipChangeRequest extends AbstractBaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    Vehicle vehicle;

    @Column(name = "initiator_id", nullable = false)
    UUID initiatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "initiator_type", nullable = false)
    private Ownership.OwnerType initiatorType;

    @Column(name = "owner_id", nullable = false)
    UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestStatus status;

    @Column(name = "reaction_date")
    LocalDateTime reactionDate;

    @Column(name = "reacted_by")
    UUID reactedBy;

    public enum RequestStatus {
        PENDING, ACCEPTED, DECLINED, COMPLETED
    }
}
