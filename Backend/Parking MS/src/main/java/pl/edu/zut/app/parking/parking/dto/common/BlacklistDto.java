package pl.edu.zut.app.parking.parking.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.edu.zut.app.parking.parking.entities.Blacklist;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * A Data Transfer Object (DTO) representing a Blacklist entity.
 * This record encapsulates data related to a blacklist entry, including
 * the unique identifier, associated vehicle information, plate number,
 * reason for blacklisting, and timestamps for creation and updates.
 *
 * The class is immutable and can be serialized/deserialized from JSON.
 * It includes a factory method for creating an instance from a Blacklist entity.
 *
 * This is primarily used in communication between different application layers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BlacklistDto(
        UUID id,
        UUID vehicleId,
        String plateNumber,
        String reason,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlacklistDto from(Blacklist save) {
        return new BlacklistDto(
                save.getId(),
                save.getVehicleId(),
                save.getPlateNumber(),
                save.getReason(),
                save.getCreationDate(),
                save.getModificationDate()
        );
    }
}
