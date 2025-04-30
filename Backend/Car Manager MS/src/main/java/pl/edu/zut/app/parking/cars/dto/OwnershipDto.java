package pl.edu.zut.app.parking.cars.dto;

import pl.edu.zut.app.parking.cars.entities.Ownership;

import java.time.LocalDateTime;
import java.util.UUID;

public record OwnershipDto(
        UUID id,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        Ownership.RecordStatus recordStatus,
        UUID vehicleId,
        UUID ownerId,
        Ownership.OwnerType ownerType,
        int version
) {
    public static OwnershipDto fromEntity(Ownership ownership) {
        return new OwnershipDto(
                ownership.getId(),
                ownership.getCreationDate(),
                ownership.getModificationDate(),
                ownership.getRecordStatus(),
                ownership.getVehicle().getId(),
                ownership.getOwnerId(),
                ownership.getOwnerType(),
                ownership.getVersion()
        );
    }
}
