package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.parking.dto.common.GateDto;
import pl.edu.zut.app.parking.parking.entities.Gate;

import java.util.List;
import java.util.UUID;

public interface GateRepository extends JpaRepository<Gate, UUID> {

    /**
     * Retrieves a list of GateDto objects associated with the specified parking ID.
     *
     * @param parkingId the unique identifier of the parking entity to find gates for
     * @return a list of GateDto objects representing gates linked to the given parking ID
     */
    @Query("select g from Gate g where g.parking.id = ?1")
    List<Gate> findAllByParking_id(UUID parkingId);
}
