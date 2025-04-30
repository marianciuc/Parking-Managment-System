package pl.zut.edu.app.parking.sessions.repositories;

import feign.ResponseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import pl.zut.edu.app.parking.sessions.entities.Session;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionsRepository
    extends JpaRepository<Session, UUID>, JpaSpecificationExecutor<Session> {
  Optional<Session> findByPlateNumber(String plate);

  long countSessionByStatusAndParkingId(Session.SessionStatus status, UUID parkingId);

  List<Session> parkingId(UUID parkingId);

  @Query(
          value =
                  "SELECT AVG(EXTRACT(EPOCH FROM (s.end_time - s.start_time)) / 60) " +
                          "FROM sessions s " +
                          "WHERE s.parking_id = :parkingId AND s.status = 'FINISHED'",
          nativeQuery = true)
  Double calculateAverageTimeOfSessionsWhereParkingId(@Param("parkingId") UUID parkingId);

  @Query("select count(s) from Session s where s.startTime > ?1")
  long countSessionByStartTimeAfter(LocalDateTime startTimeAfter);

  @Query("select count(s) from Session s where s.startTime > ?1 and s.parkingId = ?2")
  long countSessionByStartTimeAfterAndParkingId(LocalDateTime startTimeAfter, UUID parkingId);

  @Query("select s from Session s where s.plateNumber = ?1 and s.status in ?2")
  Optional<Session> findByPlateNumberAndStatusIn(
      String plateNumber, Collection<Session.SessionStatus> statuses);

  @Query("select s from Session s")
  Page<Session> findAll(Specification<Session> sessionSpecification, Pageable pageable);

  @Query("SELECT s FROM Session s WHERE s.vehicleId = :vehicleId AND s.status IN :statuses")
  List<Session> findByVehicleIdAndStatusIn(
      @Param("vehicleId") UUID vehicleId,
      @Param("statuses") Collection<Session.SessionStatus> statuses);

  @Query("select s from Session s where s.parkingId = ?1")
  Page<Session> findByParkingId(UUID parkingId, Pageable pageable);

  long countSessionByStartTimeAfterAndParkingIdAndStatusIn(LocalDateTime startTimeAfter, UUID parkingId, Collection<Session.SessionStatus> statuses);
}
