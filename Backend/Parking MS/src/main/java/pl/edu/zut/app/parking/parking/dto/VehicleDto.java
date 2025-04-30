package pl.edu.zut.app.parking.parking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleDto(
        UUID id,
        String brand,
        String model,
        String plateNumber,
        String color,
        Boolean hasOwner,
        Integer yearOfProduction,
        String vin,
        String imageUrl,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) {
    public static VehicleDto buildWithPlateNumber(String plate) {
        return new VehicleDto(null, null, null, plate, null, null, null, null, null, null, null );
    }
}
