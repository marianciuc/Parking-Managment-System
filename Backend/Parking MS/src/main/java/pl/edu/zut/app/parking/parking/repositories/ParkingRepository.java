package pl.edu.zut.app.parking.parking.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;

public interface ParkingRepository
    extends JpaRepository<Parking, UUID>, JpaSpecificationExecutor<Parking> {

  @EntityGraph(attributePaths = "tags")
  Optional<Parking> findById(UUID id);

  Page<Parking> findAll(Specification<Parking> specification, Pageable pageable);

  Optional<UUID> findByOwnerId(UUID ownerId);

  List<Parking> findAllByOwnerId(UUID id);

  Optional<Parking> findByIdAndRecordStatusIsNot(
      UUID parkingId, AbstractBaseEntity.RecordStatus recordStatus);

  Optional<Parking> findAllByOwnerIdAndRecordStatus(
      UUID ownerId, AbstractBaseEntity.RecordStatus recordStatus);

  List<Parking> findAllByOwnerIdAndRecordStatusIsNot(
      UUID ownerId, AbstractBaseEntity.RecordStatus recordStatus);
}