package pl.edu.zut.app.parking.parking.services;

import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.AddressAlreadyExistsException;
import pl.edu.zut.app.parking.parking.exceptions.AddressNotFoundException;
import pl.edu.zut.app.parking.parking.exceptions.InvalidOperationException;

import java.util.UUID;

/**
 * The AddressService interface provides methods for managing address details associated with parking.
 */
public interface AddressService {

    /**
     * Retrieves an address associated with the specified parking ID.
     *
     * @param parkingId the unique identifier of the parking for which the address is being retrieved
     * @return an AddressDto object containing the address details for the specified parking
     * @throws AddressNotFoundException if the address is not found for the specified parking ID
     */
    AddressDto find(UUID parkingId);

    /**
     * Updates the address details for the specified unique identifier.
     *
     * @param id the unique identifier of the address to be updated
     * @param addressDto an AddressDto object containing new address information
     * @return the updated AddressDto object
     * @throws AddressNotFoundException if the address is not found for the specified unique identifier
     */
    AddressDto update(UUID id, AddressDto addressDto);

    /**
     * Deletes an entity identified by the specified unique identifier.
     *
     * @param id the unique identifier of the entity to be deleted
     * @throws AddressNotFoundException if the address is not found for the specified unique identifier
     * @throws InvalidOperationException if the address has already been deleted
     */
    void delete(UUID id) throws AddressNotFoundException, InvalidOperationException;

    void deletePermanent(UUID id) throws AddressNotFoundException;

    /**
     * Creates a new address associated with the specified parking.
     *
     * @param parking the Parking entity to which the address will be associated
     * @param address an AddressDto object containing the address details
     * @throws AddressAlreadyExistsException if an address already exists for the specified parking
     */
    AddressDto create(Parking parking, AddressDto address);
}
