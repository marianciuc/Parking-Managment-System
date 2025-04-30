package pl.edu.zut.app.parking.cars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.cars.entities.OwnershipChangeRequest;

import java.util.UUID;

public interface OwnershipChangeRequestRepository extends JpaRepository<OwnershipChangeRequest, UUID> {
    boolean existsByVehicleIdAndOwnerId(UUID vehicleId, UUID ownerId);
}
