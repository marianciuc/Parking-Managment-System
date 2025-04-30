package pl.edu.zut.app.parking.cars.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.cars.entities.Vehicle;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {

    boolean existsByPlateNumber(String plateNumber);

    boolean existsByPlateNumberIgnoreCase(String plateNumber);

    Optional<Vehicle> findByPlateNumberIgnoreCase(@NotBlank(message = "Plate number cannot be blank") @Pattern(regexp = "^[A-Z0-9-]+$", message = "Plate number must consist of uppercase letters, numbers, and dashes only") @Size(min = 5, max = 10, message = "Plate number must be between 5 and 10 characters") String s);

    @Query("select v from Vehicle v")
    Page<Vehicle> findAll(Specification<Vehicle> specification, Pageable pageable);

    @Query("select v from Vehicle v where v.ownership.ownerId = ?1")
    Page<Vehicle> findByOwnership_OwnerId(UUID ownerId, Pageable pageable);
}
