package pl.edu.zut.app.parking.owners.services;

import pl.edu.zut.app.parking.owners.entities.Address;

import java.util.UUID;

/**
 * Service interface for managing Address entities in the repository.
 * Provides methods to save and retrieve Address records.
 */
public interface AddressRepositoryService {

    /**
     * Saves the given Address entity to the repository.
     *
     * @param address the Address entity to save
     * @return the saved Address entity
     */
    Address save(Address address);

    /**
     * Finds and retrieves an Address entity by its unique identifier.
     *
     * @param id the unique UUID of the Address entity to find
     * @return the Address entity associated with the given UUID, or null if not found
     */
    Address findById(UUID id);
}
