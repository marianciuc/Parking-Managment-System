package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.parking.entities.CapacityDetails;

import java.util.Optional;
import java.util.UUID;

public interface CapacityDetailsRepository extends JpaRepository<CapacityDetails, UUID> {
    Optional<CapacityDetails> findCapacityDetailsByParkingId(UUID id);

    boolean existsByParkingId(UUID id);
}
