package pl.edu.zut.app.parking.parking.services;

import pl.edu.zut.app.parking.parking.dto.common.ParkingCapacityDto;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.enums.PlaceType;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;


import java.util.UUID;

/**
 * A service interface for managing capacity details for parking entities.
 */
public interface CapacityService {

    ParkingCapacityDto getCapacityDetails(UUID id);

    void resetCapacity(UUID id);

    ParkingCapacityDto updateCapacityDetails(UUID id, ParkingCapacityDto parkingCapacityDto);

    void createCapacity(Parking parking);

    void deleteCapacityDetails(Parking parking);

    void carOut(UUID id, int increment, PlaceType placeType);

    void carIn(UUID id, int decrement, PlaceType placeType);

    void deleteCapacity(UUID id) throws NotFoundException;
}
