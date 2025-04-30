package pl.edu.zut.app.parking.owners.services;

import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.requests.PersonalDataRegistrationRequest;
import pl.edu.zut.app.parking.owners.exceptions.ParkingOwnerAlreadyExistsException;

import java.util.UUID;

/**
 * Service interface for registering parking owners into the system.
 */
public interface ParkingOwnerRegistrationService {

    /**
     * Creates a new parking owner with the given ID.
     *
     * @param ownerId the ID of the parking owner to create.
     * @throws ParkingOwnerAlreadyExistsException if the owner already exists.
     */
    void createParkingOwner(UUID ownerId);

    /**
     * Fills personal data for the parking owner with the provided request.
     *
     * @param request the request containing personal owner data.
     */
    void fillOwnerData(PersonalDataRegistrationRequest request);

    /**
     * Registers an address for the parking owner.
     *
     * @param addressDto the DTO containing address details.
     */
    void fillOwnerAddress(AddressDto addressDto);
}
