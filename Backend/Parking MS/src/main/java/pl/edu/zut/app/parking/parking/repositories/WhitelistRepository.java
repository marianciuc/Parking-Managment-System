package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.edu.zut.app.parking.parking.entities.AbstractVehicleList;
import pl.edu.zut.app.parking.parking.entities.Whitelist;

import java.util.Optional;
import java.util.UUID;

public interface WhitelistRepository extends JpaRepository<Whitelist, UUID>, JpaSpecificationExecutor<Whitelist> {
    Boolean existsByParkingIdAndPlateNumber(UUID parkingId, String vehiclePlate);

    Page<Whitelist> findAll(Specification<Whitelist> specification, Pageable pageable);

    Optional<Whitelist> findByVehicleIdAndParkingId(UUID vehicleId, UUID parkingId);
}
