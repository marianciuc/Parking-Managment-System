package pl.edu.zut.app.parking.owners.services;

import pl.edu.zut.app.parking.owners.entities.Owner;
import pl.edu.zut.app.parking.owners.exceptions.ParkingOwnerNotFoundException;

import java.util.UUID;

/**
 * Service interface for managing parking owner entities in the repository.
 * Provides methods to find, save, and check the existence of owners.
 */
public interface OwnerRepositoryService {

    /**
     * Finds an owner by their ID.
     *
     * @param ownerId the ID of the parking owner
     * @return the Owner entity if found
     * @throws ParkingOwnerNotFoundException if the entity was not found
     */
    Owner findOwnerById(UUID ownerId);

    /**
     * Saves the owner entity to the repository.
     *
     * @param owner the owner entity to save
     * @return the saved owner entity
     */
    Owner save(Owner owner);


    /**
     * Checks if an owner exists by their ID.
     *
     * @param ownerId the ID of the parking owner
     * @return `true` if the owner exists, `false` otherwise
     */
    boolean existsById(UUID ownerId);

    /**
     * Checks if an owner exists by their owner ID.
     *
     * @param ownerId the unique identifier of the parking owner
     * @return true if an owner with the specified owner ID exists, false otherwise
     */
    boolean existsByOwnerId(UUID ownerId);
}
