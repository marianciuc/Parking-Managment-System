package pl.edu.zut.app.parking.parking.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.zut.app.parking.parking.entities.OpeningHours;

import java.util.List;
import java.util.UUID;

public interface OpeningHoursRepository extends JpaRepository<OpeningHours, UUID> {
    @EntityGraph(attributePaths = {"parking"})
    List<OpeningHours> findAllByParkingId(UUID parkingId);
}
