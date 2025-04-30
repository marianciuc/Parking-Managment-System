package pl.edu.zut.app.parking.cars.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.zut.app.parking.cars.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.cars.entities.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleDto(
        UUID id,
        String brand,
        String model,
        @NotBlank(message = "Plate number cannot be blank")
        @Pattern(regexp = "^[A-Z0-9-]+$", message = "Plate number must consist of uppercase letters, numbers, and dashes only")
        @Size(min = 5, max = 10, message = "Plate number must be between 5 and 10 characters")
        String plateNumber,
        String color,
        boolean hasOwner,
        Integer yearOfProduction,
        String vin,
        OwnershipDto ownership,
        String imageUrl,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        AbstractBaseEntity.RecordStatus recordStatus,
        int version
) {
    private static final Logger logger = LoggerFactory.getLogger(VehicleDto.class);

    public Vehicle fromDto() {
        logger.info("Converting VehicleDto to Vehicle entity. Vehicle ID: {}", id);
        return Vehicle.builder()
                .id(id)
                .brand(brand)
                .model(model)
                .plateNumber(plateNumber)
                .color(color)
                .hasOwner(hasOwner)
                .yearOfProduction(yearOfProduction)
                .vin(vin)
                .imageUrl(imageUrl)
                .creationDate(creationDate)
                .modificationDate(modificationDate)
                .recordStatus(recordStatus)
                .version(version)
                .build();
    }

    public static VehicleDto fromEntity(Vehicle vehicle) {
        if (vehicle == null) {
            logger.warn("Attempted to convert a null Vehicle entity to VehicleDto.");
            return null;
        }
        logger.info("Converting Vehicle entity to VehicleDto. Vehicle ID: {}", vehicle.getId());
        return new VehicleDto(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getPlateNumber(),
                vehicle.getColor(),
                vehicle.isHasOwner(),
                vehicle.getYearOfProduction(),
                vehicle.getVin(),
                vehicle.getOwnership() != null ? OwnershipDto.fromEntity(vehicle.getOwnership()) : null,
                vehicle.getImageUrl(),
                vehicle.getCreationDate(),
                vehicle.getModificationDate(),
                vehicle.getRecordStatus(),
                vehicle.getVersion()
        );
    }

}
