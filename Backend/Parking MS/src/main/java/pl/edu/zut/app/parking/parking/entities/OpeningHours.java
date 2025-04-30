package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.parking.exceptions.InvalidOpeningHoursException;

import java.time.LocalTime;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "opening_hours")
public class OpeningHours extends AbstractBaseEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "is_closed")
    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id", nullable = false, referencedColumnName = "id")
    private Parking parking;

    @PrePersist
    @PreUpdate
    private void validate() {
        boolean timesShouldBeNull = isClosed && (openingTime != null || closingTime != null);
        boolean timesAreRequired = !isClosed && (openingTime == null || closingTime == null);

        if (timesShouldBeNull) {
            throw new InvalidOpeningHoursException("Opening and closing times should be null if the parking is closed.");
        }

        if (timesAreRequired) {
            throw new InvalidOpeningHoursException("Opening and closing times must be set if the parking is open.");
        }
    }

    public enum DayOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }
}
