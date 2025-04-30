package pl.edu.zut.app.parking.parking.entities;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractVehicleList extends AbstractBaseEntity {

    @NotBlank(message = "Plate number cannot be blank")
    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parking_id", nullable = false)
    private Parking parking;
}
