package pl.edu.zut.app.parking.cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.edu.zut.app.parking.cars.entities.Ownership;

import java.util.Optional;
import java.util.UUID;

public interface OwnershipRepository extends JpaRepository<Ownership, UUID>, JpaSpecificationExecutor<Ownership> {
    boolean existsByVehicleIdAndOwnerId(UUID vehicleId, UUID ownerId);


    Optional<Ownership> findByVehicleId(UUID carId);
}
