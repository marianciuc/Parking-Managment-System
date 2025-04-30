package pl.edu.zut.app.parking.owners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.owners.entities.Owner;

import java.util.Optional;
import java.util.UUID;

public interface ParkingOwnerRepository extends JpaRepository<Owner, UUID> {
    boolean existsByUserId(UUID ownerId);

    Optional<Owner> findOwnerByUserId(UUID ownerId);
}
