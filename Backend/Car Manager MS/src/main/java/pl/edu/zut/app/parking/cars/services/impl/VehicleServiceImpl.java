package pl.edu.zut.app.parking.cars.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.cars.dto.VehicleDto;
import pl.edu.zut.app.parking.cars.dto.VehicleSearchRequest;
import pl.edu.zut.app.parking.cars.entities.Vehicle;
import pl.edu.zut.app.parking.cars.exceptions.InvalidVehicleDataException;
import pl.edu.zut.app.parking.cars.exceptions.VehicleAlreadyExistsException;
import pl.edu.zut.app.parking.cars.exceptions.VehicleNotFoundException;
import pl.edu.zut.app.parking.cars.repositories.VehicleRepository;
import pl.edu.zut.app.parking.cars.services.OwnershipService;
import pl.edu.zut.app.parking.cars.services.VehicleService;
import pl.edu.zut.app.parking.cars.specifications.VehicleSpecifications;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OwnershipService ownershipService;

    /**
     * This method finds a vehicle by plate number and returns it as VehicleDto.
     *
     * @param plate the plate number of the vehicle
     * @return VehicleDto - found vehicle
     * @throws VehicleNotFoundException when vehicle with given plate number does not exist
     */
    @Override
    public VehicleDto findByPlate(String plate) {
        log.debug("Searching for vehicle by plate number: {}", plate);
        return VehicleDto.fromEntity(findVehicleByPlateOrThrow(plate));
    }

    /**
     * This method finds vehicle by id and returns it as VehicleDto.
     *
     * @param vehicleId the id of the vehicle
     * @return VehicleDto - found vehicle
     * @throws VehicleNotFoundException
     */
    @Override
    public VehicleDto findById(UUID vehicleId) {
        log.debug("Searching for vehicle by ID: {}", vehicleId);
        return VehicleDto.fromEntity(findVehicleOrThrow(vehicleId));
    }

    private Vehicle findVehicleOrThrow(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with id " + id + " not found"));
    }

    private Vehicle findVehicleByPlateOrThrow(String plate) {
        return vehicleRepository.findByPlateNumberIgnoreCase(plate)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with plate number " + plate + " not found"));
    }

    /**
     * This method updates vehicle with given id and returns it as VehicleDto.
     *
     * @param vehicleId
     * @param vehicleDto
     * @return VehicleDto - updated vehicle
     * @throws VehicleNotFoundException    when vehicle with given id does not exist
     * @throws InvalidVehicleDataException when vehicle data is invalid
     */
    @Override
    public VehicleDto update(UUID vehicleId, VehicleDto vehicleDto) {
        log.debug("Updating vehicle with ID: {} and data: {}", vehicleId, vehicleDto);

        Vehicle vehicle = findVehicleOrThrow(vehicleId);
        log.debug("Vehicle found for update: ID = {}", vehicleId);
        updateFieldIfPresent(vehicleDto.color(), vehicle::setColor);
        updateFieldIfPresent(vehicleDto.brand(), vehicle::setBrand);
        updateFieldIfPresent(vehicleDto.model(), vehicle::setModel);
        updateFieldIfPresent(vehicleDto.yearOfProduction(), vehicle::setYearOfProduction);
        updateFieldIfPresent(vehicleDto.vin(), vehicle::setVin);
        updateFieldIfPresent(vehicleDto.imageUrl(), vehicle::setImageUrl);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle updated successfully: ID = {}", savedVehicle.getId());
        return VehicleDto.fromEntity(savedVehicle);
    }

    /**
     * Helper method to update a field if present.
     */
    private <T> void updateFieldIfPresent(T value, Consumer<T> setter) {
        log.debug("Checking field for update. Value: {}", value);
        if (value != null && (!(value instanceof String) || !((String) value).isEmpty())) {
            setter.accept(value);
        }
    }

    /**
     * This method creates a new vehicle and returns it as VehicleDto.
     *
     * @param vehicleDto
     * @return VehicleDto - created vehicle
     * @throws InvalidVehicleDataException   when vehicle data is invalid
     * @throws VehicleAlreadyExistsException when vehicle with given plate number already exists
     */
    @Override
    @Transactional
    public VehicleDto create(VehicleDto vehicleDto, Boolean assignOwnership) {
        log.debug("Initiating vehicle creation process. Data: {}, AssignOwnership: {}", vehicleDto, assignOwnership);
        if (vehicleDto == null) {
            log.error("Invalid vehicle data provided: {}", vehicleDto);
            throw new InvalidVehicleDataException("Vehicle data is invalid");
        }

        log.info("Trying to create a vehicle with plate number: {}", vehicleDto.plateNumber());
        log.info("Attempting to create a vehicle with plate number: {}", vehicleDto.plateNumber());
        log.debug("Checking if the plate number already exists in the database");
        return vehicleRepository.findByPlateNumberIgnoreCase(vehicleDto.plateNumber())
                .map(existingVehicle -> handleExistingVehicle(existingVehicle, assignOwnership))
                .orElseGet(() -> handleNewVehicle(vehicleDto, assignOwnership));
    }

    private VehicleDto handleExistingVehicle(Vehicle vehicle, Boolean assignOwnership) {
        log.debug("Handling existing vehicle operation. Vehicle: {}, AssignOwnership: {}", vehicle, assignOwnership);

        if (!assignOwnership) {
            log.warn("Vehicle exists but assign ownership is disabled. Vehicle ID: {}", vehicle.getId());
            throw new VehicleAlreadyExistsException("Vehicle with the given plate number already exists");
        }
        log.info("Vehicle exists; initiating ownership transfer for ID: {}", vehicle.getId());
        log.info("Requesting ownership transfer for vehicle ID: {}", vehicle.getId());
        ownershipService.requestOwnershipTransfer(vehicle.getId());
        return VehicleDto.fromEntity(vehicle);
    }

    private VehicleDto handleNewVehicle(VehicleDto vehicleDto, Boolean assignOwnership) {
        log.debug("Handling new vehicle creation. Data: {}", vehicleDto);
        Vehicle newVehicle = Vehicle.builder()
                .plateNumber(vehicleDto.plateNumber())
                .hasOwner(false)
                .recordStatus(Vehicle.RecordStatus.ACTIVE)
                .build();
        log.info("Saving new vehicle with plate number: {}", vehicleDto.plateNumber());
        log.debug("Vehicle details to be saved: {}", vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(newVehicle);
        update(savedVehicle.getId(), vehicleDto);
        log.info("Vehicle saved successfully with ID: {}", savedVehicle.getId());
        if (assignOwnership) {
            log.info("Assigning ownership to the vehicle with ID: {}", savedVehicle.getId());
            ownershipService.requestOwnershipTransfer(savedVehicle.getId());
        }
        return VehicleDto.fromEntity(vehicleRepository.findById(savedVehicle.getId()).orElseThrow());
    }

    @Override
    public Page<VehicleDto> find(int page, int size, VehicleSearchRequest request) {
        log.info("Starting search for vehicles. Page: {}, Size: {}, Request: {}", page, size, request);
        if (request == null) {
            throw new IllegalArgumentException("Search request cannot be null");
        }

        log.info("Executing search for vehicles. Page: {}, Size: {}", page, size);

        Specification<Vehicle> specification = Specification.where(VehicleSpecifications.whereColor(request.color()))
                .and(VehicleSpecifications.whereBrand(request.brand()))
                .and(VehicleSpecifications.whereModel(request.model()))
                .and(VehicleSpecifications.whereOwnerId(request.ownerId()))
                .and(VehicleSpecifications.whereIdEquals(request.id()))
                .and(VehicleSpecifications.whereOwnerType(request.ownerType()))
                .and(VehicleSpecifications.wherePlateNumber(request.plateNumber()));

        log.info("Executing search for vehicles. Page: {}, Size: {}", page, size);
        Page<VehicleDto> result = vehicleRepository.findByOwnership_OwnerId(request.ownerId(),
                        Pageable.ofSize(size).withPage(page))
                .map(VehicleDto::fromEntity);
        log.info("Vehicle search completed. Found {} results", result.getTotalElements());
        log.info("result: {} ", result);
        return result;
    }
}
