package pl.edu.zut.app.parking.parking.dto.common;

import pl.edu.zut.app.parking.parking.dto.requests.ParkingCreateRequest;
import pl.edu.zut.app.parking.parking.entities.CapacityDetails;

public record ParkingCapacityDto(
        int capacity,
        int occupiedSpaces,
        int placesForDisabled,
        int occupiedPlacesForDisabled,
        int placesForElectricCars,
        int occupiedPlacesForElectricCars
) {
    public static ParkingCapacityDto fromEntity(CapacityDetails capacityDetails) {
        return new ParkingCapacityDto(
                capacityDetails.getCapacity(),
                capacityDetails.getOccupiedSpaces(),
                capacityDetails.getPlacesForDisabled(),
                capacityDetails.getOccupiedPlacesForDisabled(),
                capacityDetails.getPlacesForElectricCars(),
                capacityDetails.getOccupiedPlacesForElectricCars()
        );
    }
}
