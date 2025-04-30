package pl.edu.zut.app.parking.owners.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.owners.entities.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
