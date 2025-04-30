package pl.edu.zut.app.parking.cars.dto;

import pl.edu.zut.app.parking.cars.entities.Ownership;

import java.util.UUID;

public record VehicleSearchRequest(
        UUID id,
        String plateNumber,
        String brand,
        String model,
        String color,
        UUID ownerId,
        Ownership.OwnerType ownerType,
        Integer yearOfProduction
) {
}
