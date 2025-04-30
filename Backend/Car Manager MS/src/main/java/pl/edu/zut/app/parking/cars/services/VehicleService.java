package pl.edu.zut.app.parking.cars.services;

import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.cars.dto.VehicleDto;
import pl.edu.zut.app.parking.cars.dto.VehicleSearchRequest;
import pl.edu.zut.app.parking.cars.exceptions.InvalidVehicleDataException;
import pl.edu.zut.app.parking.cars.exceptions.VehicleAlreadyExistsException;
import pl.edu.zut.app.parking.cars.exceptions.VehicleNotFoundException;

import java.util.UUID;

public interface VehicleService {

    /**
     * This method finds vehicle by plate number and returns it as VehicleDto.
     *
     * @param plate the plate number of the vehicle
     * @return VehicleDto - found vehicle
     * @throws VehicleNotFoundException when vehicle with given plate number does not exist
     */
    VehicleDto findByPlate(String plate);

    /**
     * This method finds vehicle by id and returns it as VehicleDto.
     *
     * @param vehicleId the id of the vehicle
     * @return VehicleDto - found vehicle
     * @throws VehicleNotFoundException
     */
    VehicleDto findById(UUID vehicleId);


    /**
     * This method updates vehicle with given id and returns it as VehicleDto.
     *
     * @param vehicleId
     * @param vehicleDto
     * @return VehicleDto - updated vehicle
     * @throws VehicleNotFoundException when vehicle with given id does not exist
     * @throws InvalidVehicleDataException when vehicle data is invalid
     */
    VehicleDto update(UUID vehicleId, VehicleDto vehicleDto);

    /**
     * This method creates new vehicle and returns it as VehicleDto.
     *
     * @param vehicleDto
     * @return VehicleDto - created vehicle
     * @throws InvalidVehicleDataException when vehicle data is invalid
     * @throws VehicleAlreadyExistsException when vehicle with given plate number already exists
     */
    VehicleDto create(VehicleDto vehicleDto, Boolean assignOwnership);

    Page<VehicleDto> find(int page, int size, VehicleSearchRequest request);
}
