package pl.zut.edu.app.parking.sessions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.zut.edu.app.parking.sessions.entities.AbstractBaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleDto(
        UUID id,
        OwnershipDto ownership,
        String brand,
        String model,
        String plateNumber,
        String color,
        Boolean hasOwner,
        Integer yearOfProduction,
        String vin,
        String imageUrl,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        AbstractBaseEntity.RecordStatus recordStatus,
        Integer version
) {
    public static VehicleDto buildWithPlateNumber(String plate) {
        return new VehicleDto(null, null, null, null, plate, null, null, null, null, null, null, null, null, null);
    }
}
