package pl.edu.zut.app.parking.parking.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.parking.dto.common.ParkingCapacityDto;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.CapacityDetails;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.enums.PlaceType;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;
import pl.edu.zut.app.parking.parking.repositories.CapacityDetailsRepository;
import pl.edu.zut.app.parking.parking.services.CapacityService;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CapacityDetailsServiceImpl implements CapacityService {

    private final CapacityDetailsRepository capacityDetailsRepository;

    @Override
    public ParkingCapacityDto getCapacityDetails(UUID parkingId) {
        return ParkingCapacityDto.fromEntity(getCapacity(parkingId));
    }

    @Override
    @Transactional
    public void resetCapacity(UUID id) {
        CapacityDetails capacityDetails = getCapacity(id);
        capacityDetails.setOccupiedSpaces(0);
        capacityDetails.setOccupiedPlacesForElectricCars(0);
        capacityDetails.setOccupiedPlacesForDisabled(0);
        capacityDetailsRepository.save(capacityDetails);

        log.info("Capacity reset completed for parking id: {}", id);
    }

    private CapacityDetails getCapacity(UUID parkingId) {
        return capacityDetailsRepository.findCapacityDetailsByParkingId(parkingId)
                .orElseThrow(() -> new NotFoundException("Parking entity with parking id: " + parkingId + " not found"));
    }

    @Override
    public ParkingCapacityDto updateCapacityDetails(UUID id, ParkingCapacityDto parkingCapacityDto) {
        CapacityDetails capacityDetails = getCapacity(id);
        if (capacityDetails.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED) {
            throw new IllegalStateException("Capacity details for parking with id: " + id + " already deleted");
        }
        if (capacityDetails.getCapacity() != parkingCapacityDto.capacity()) {
            capacityDetails.setCapacity(parkingCapacityDto.capacity());
        }
        if (capacityDetails.getPlacesForDisabled() != parkingCapacityDto.placesForDisabled()) {
            capacityDetails.setPlacesForDisabled(parkingCapacityDto.placesForDisabled());
        }
        if (capacityDetails.getPlacesForElectricCars() != parkingCapacityDto.placesForElectricCars()) {
            capacityDetails.setPlacesForElectricCars(parkingCapacityDto.placesForElectricCars());
        }
        return ParkingCapacityDto.fromEntity(capacityDetailsRepository.save(capacityDetails)) ;
    }

    @Override
    @Transactional
    public void createCapacity(Parking parking) {
        if (parking.getCapacityDetails() != null) {
            throw new IllegalStateException("Capacity details already exist for parking with id: " + parking.getId());
        }

        CapacityDetails capacityDetails = CapacityDetails.builder()
                .capacity(0)
                .placesForDisabled(0)
                .placesForElectricCars(0)
                .occupiedPlacesForDisabled(0)
                .occupiedPlacesForElectricCars(0)
                .occupiedSpaces(0)
                .parking(parking)
                .build();

        parking.setCapacityDetails(capacityDetails);
        capacityDetailsRepository.save(capacityDetails);
    }

    /**
     * Changes the status of the capacity details to DELETED
     *
     * @param id the unique identifier of the capacity details to be deleted
     */
    @Override
    public void deleteCapacityDetails(Parking parking) {
        CapacityDetails capacityDetails = getCapacity(parking.getId());
        capacityDetails.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
        capacityDetailsRepository.save(capacityDetails);
    }

    /**
     * Updates the capacity details by increasing the number of occupied parking spaces.
     *
     * @param id        the unique identifier of the parking to update
     * @param increment the number of cars leaving the parking, which will reduce the occupied spaces
     * @param placeType the type of parking space being vacated
     */
    @Override
    @Transactional
    public void carOut(UUID id, int increment, PlaceType placeType) {
        updateCapacity(id, -increment, placeType);
    }

    /**
     * Updates the capacity details by increasing the number of occupied parking spaces.
     *
     * @param id        the unique identifier of the parking to update
     * @param decrement the number of cars entering the parking, which will increase the occupied spaces
     * @param placeType the type of parking space being occupied
     */
    @Override
    @Transactional
    public void carIn(UUID id, int decrement, PlaceType placeType) {
        updateCapacity(id, decrement, placeType);
    }

    @Override
    public void deleteCapacity(UUID id) throws NotFoundException {
        if (!capacityDetailsRepository.existsById(id)) {
            throw new NotFoundException("Capacity details with id: " + id + " not found");
        }
        capacityDetailsRepository.deleteById(id);
    }


    private void updateCapacity(UUID id, int delta, PlaceType placeType) {
        CapacityDetails capacityDetails = getCapacity(id);

        switch (placeType) {
            case DISABLED:
                int updatedDisabled = capacityDetails.getOccupiedPlacesForDisabled() + delta;
                validateCapacity(updatedDisabled, capacityDetails.getPlacesForDisabled(), PlaceType.DISABLED);
                capacityDetails.setOccupiedPlacesForDisabled(updatedDisabled);
                break;

            case ELECTRIC:
                int updatedElectric = capacityDetails.getOccupiedPlacesForElectricCars() + delta;
                validateCapacity(updatedElectric, capacityDetails.getPlacesForElectricCars(), PlaceType.ELECTRIC);
                capacityDetails.setOccupiedPlacesForElectricCars(updatedElectric);
                break;

            default:
                int updatedRegular = capacityDetails.getOccupiedSpaces() + delta;
                validateCapacity(updatedRegular, capacityDetails.getCapacity(), PlaceType.REGULAR);
                capacityDetails.setOccupiedSpaces(updatedRegular);
        }

        log.info("Updated capacity for parking id: {}, placeType: {}, delta: {}, resulting capacity: {}",
                id, placeType, delta, capacityDetails);

        capacityDetailsRepository.save(capacityDetails);
    }

    private void validateCapacity(int updated, int total, PlaceType placeType) {
        if (updated < 0) {
            throw new IllegalArgumentException("Occupied " + placeType + " places cannot be negative");
        }
//        if (updated > total) {
//            throw new IllegalArgumentException("Occupied " + placeType + " places cannot exceed total capacity");
//        }
    }
}
