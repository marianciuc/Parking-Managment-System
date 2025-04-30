package pl.zut.edu.app.parking.sessions.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "sessions")
public class Session extends AbstractBaseEntity {

  @Column(name = "vehicle_id")
  private UUID vehicleId;

  @Column(name = "plate_number")
  private String plateNumber;

  @Column(name = "parking_id")
  private UUID parkingId;

  @Column(name = "owner_id")
  private UUID ownerId;

  @Column(name = "vehicle_access_list")
  private String vehicleAccessList;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "end_time")
  private LocalDateTime endTime;

  @Column(name = "paid_until")
  private LocalDateTime paidUntil;

  @Column(name = "price")
  private BigDecimal price;

  @Column(name = "tariff_id")
  private UUID tariffId;

  @Column(name = "tariff_name")
  private String tariffName;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private SessionStatus status;

  public enum SessionStatus {
    PREPARE,
    ACTIVE,
    FINISHED,
    PREPARE_END,
    CANCELLED,
    STOPPED_BY_OWNER,
    STOPPED_BY_SYSTEM
  }

  public boolean isPaid() {
    return paidUntil != null && LocalDateTime.now().isBefore(paidUntil);
  }
}
