package pl.edu.zut.app.parking.parking.repositories;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.edu.zut.app.parking.parking.entities.Blacklist;

import java.util.Optional;
import java.util.UUID;

public interface BlacklistRepository extends JpaRepository<Blacklist, UUID>, JpaSpecificationExecutor<Blacklist> {

    Optional<Blacklist> findByPlateNumber(String plateNumber);

    Optional<Blacklist> findByPlateNumberAndParkingId(String plateNumber, UUID parkingId);

    boolean existsByParkingIdAndPlateNumber(UUID attr0, @NotBlank(message = "Plate number cannot be blank") String plateNumber);

    Page<Blacklist> findAll(Specification<Blacklist> specification, Pageable of);
}
