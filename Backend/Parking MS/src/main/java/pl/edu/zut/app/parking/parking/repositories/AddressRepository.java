package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.entities.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByParkingId(UUID parkingId);
}
