package pl.edu.zut.app.parking.cars.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "ownerships",
        indexes = {
                @Index(name = "vehicle_id_index", columnList = "vehicle_id"),
                @Index(name = "owner_id_index", columnList = "owner_id")
}
)
public class Ownership extends AbstractBaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    public enum OwnerType {
        PERSON, FAMILY
    }
}
