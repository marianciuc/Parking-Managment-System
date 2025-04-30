package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder
@Entity
@Table(name = "gates")
public class Gate extends AbstractBaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GageType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GateStatus status;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private int port;

    @Column(name = "is_manual_mode")
    private boolean isManualMode;

    public enum GageType {
        IN, OUT
    }

    public enum GateStatus {
        OPEN, CLOSED
    }
}
