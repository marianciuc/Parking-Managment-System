package pl.edu.zut.app.parking.owners.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.owners.entities.Owner;
import pl.edu.zut.app.parking.owners.exceptions.ParkingOwnerNotFoundException;
import pl.edu.zut.app.parking.owners.repositories.ParkingOwnerRepository;
import pl.edu.zut.app.parking.owners.services.OwnerRepositoryService;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OwnerRepositoryServiceImpl implements OwnerRepositoryService {

    private final ParkingOwnerRepository repository;

//    @Cacheable(value = "ownerById", key = "#ownerId")
    @Override
    public Owner findOwnerById(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
        log.info("Searching for Owner with id: {}", ownerId);
        return repository.findOwnerByUserId(ownerId).orElseThrow(() -> new ParkingOwnerNotFoundException(ownerId));
    }

    @CachePut(value = "owners", key = "#owner.id")
    @Override
    public Owner save(Owner owner) {
        return repository.save(owner);
    }

    @Cacheable(value = "ownerById", key = "#ownerId")
    @Override
    public boolean existsById(UUID ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
        log.info("Checking existence of Owner with id: {}", ownerId);
        return repository.existsById(ownerId);
    }

    /**
     * Checks if an owner exists by their owner ID.
     *
     * @param ownerId the unique identifier of the parking owner
     * @return true if an owner with the specified owner ID exists, false otherwise
     */
    @Cacheable(value = "ownerById", key = "#ownerId")
    @Override
    public boolean existsByOwnerId(UUID ownerId) {
        return repository.existsByUserId(ownerId);
    }
}
