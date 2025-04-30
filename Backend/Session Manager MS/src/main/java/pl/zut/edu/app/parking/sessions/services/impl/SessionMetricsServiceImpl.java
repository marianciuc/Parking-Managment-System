package pl.zut.edu.app.parking.sessions.services.impl;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zut.edu.app.parking.sessions.dto.response.MetricsDto;
import pl.zut.edu.app.parking.sessions.dto.response.PeakHours;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehicleStatsDto;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehiclesInPeakHoursDto;
import pl.zut.edu.app.parking.sessions.entities.Session;
import pl.zut.edu.app.parking.sessions.repositories.SessionsRepository;
import pl.zut.edu.app.parking.sessions.services.SessionMetricsService;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionMetricsServiceImpl implements SessionMetricsService {

  private final SessionsRepository sessionsRepository;
  private final EntityManager entityManager;

  @Override
  public MetricsDto getMetricsForParking(UUID parkingId) {
    Integer amountOfActiveSessions = getAmountOfActiveSessions(parkingId);
    Double averageTimeOfActiveSessions = calculateAverageTimeOfSessions(parkingId);
    Integer totalSessionsForWeek = getTotalSessionsForWeek(parkingId);
    Integer totalSessionsForMonth = getTotalSessionsForMonth(parkingId);
    Integer totalSessionsForYear = getTotalSessionsForYear(parkingId);
    Integer totalSessionsForAllTime = getTotalSessionsForAllTime(parkingId);
    List<TopVehicleStatsDto> personalTariffProposition = getPersonalTariffProposition(parkingId);
    List<PeakHours> peakHours = getPeakHours(parkingId);
    List<TopVehiclesInPeakHoursDto> topVehiclesInPeakHours = getTopVehiclesInPeakHours(parkingId);

    return new MetricsDto(
        amountOfActiveSessions,
        averageTimeOfActiveSessions,
        totalSessionsForWeek,
        totalSessionsForMonth,
        totalSessionsForYear,
        totalSessionsForAllTime,
        personalTariffProposition,
        peakHours,
        topVehiclesInPeakHours);
  }

  public Integer getAmountOfActiveSessions(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStatusAndParkingId(
            Session.SessionStatus.ACTIVE, parkingId));
  }

  public Double calculateAverageTimeOfSessions(UUID parkingId) {
    return sessionsRepository.calculateAverageTimeOfSessionsWhereParkingId(parkingId);
  }

  public Integer getAmountOfFinishedSessions(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStatusAndParkingId(
            Session.SessionStatus.FINISHED, parkingId));
  }

  public Integer getAmountOfCancelledSessions(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStatusAndParkingId(
            Session.SessionStatus.CANCELLED, parkingId));
  }

  public Integer getTotalSessionsForWeek(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStartTimeAfterAndParkingId(
            LocalDateTime.now().minusDays(7), parkingId));
  }

  public Integer getTotalSessionsForMonth(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStartTimeAfterAndParkingId(
            LocalDateTime.now().minusMonths(1), parkingId));
  }

  public Integer getTotalSessionsForYear(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStartTimeAfterAndParkingId(
            LocalDateTime.now().minusYears(1), parkingId));
  }

  public Integer getTotalSessionsForAllTime(UUID parkingId) {
    return Math.toIntExact(
        sessionsRepository.countSessionByStartTimeAfterAndParkingIdAndStatusIn(
            LocalDateTime.now().minusYears(100),
            parkingId,
            List.of(Session.SessionStatus.FINISHED, Session.SessionStatus.ACTIVE)));
  }

  public List<TopVehicleStatsDto> getPersonalTariffProposition(UUID parkingId) {
    List<Object[]> resultList =
        entityManager
            .createNativeQuery(
                "SELECT s.plate_number, "
                    + "       CASE "
                    + "           WHEN COUNT(DISTINCT DATE_TRUNC('month', s.start_time)) = 0 THEN 0 "
                    + "           ELSE COUNT(*)::float / COUNT(DISTINCT DATE_TRUNC('month', s.start_time)) "
                    + "       END AS avg_sessions_per_month, "
                    + "       s.vehicle_id "
                    + "FROM sessions s "
                    + "WHERE s.parking_id = :parkingId "
                    + "GROUP BY s.plate_number, s.vehicle_id "
                    + "ORDER BY avg_sessions_per_month DESC "
                    + "LIMIT 5")
            .setParameter("parkingId", parkingId)
            .getResultList();

    log.info("Result list: {}", resultList);

    if (resultList == null || resultList.isEmpty()) {
      log.warn("No data available for parkingId {}", parkingId);
      return List.of();
    }

    return resultList.stream()
        .map(
            row ->
                new TopVehicleStatsDto(
                    (UUID) row[2], (String) row[0], ((Number) row[1]).doubleValue()))
        .toList();
  }

  public List<PeakHours> getPeakHours(UUID parkingId) {
    List<Object[]> resultList =
        entityManager
            .createQuery(
                "SELECT COALESCE(EXTRACT(DAY FROM s.startTime), -1) as day_of_week,"
                    + " COALESCE(EXTRACT(HOUR FROM s.startTime), -1) as hour_of_day,"
                    + " COUNT(*) as count_of_sessions"
                    + " FROM Session s "
                    + " WHERE s.parkingId = :parkingId AND s.startTime is not null "
                    + " GROUP BY day_of_week, hour_of_day "
                    + " ORDER BY count_of_sessions DESC",
                Object[].class)
            .setParameter("parkingId", parkingId)
            .getResultList();

    log.info("Result list: {}", resultList);
    if (resultList == null || resultList.isEmpty()) {
      log.warn("No data available for parkingId {}", parkingId);
      return List.of();
    }

    return resultList.stream()
        .map(
            row ->
                new PeakHours(
                    ((Number) row[0]).intValue(),
                    ((Number) row[1]).intValue(),
                    ((Number) row[2]).longValue()))
        .toList();
  }

  public List<TopVehiclesInPeakHoursDto> getTopVehiclesInPeakHours(UUID parkingId) {
    List<Object[]> resultList =
        entityManager
            .createQuery(
                "SELECT vehicleId, plateNumber, count(*) as session_count "
                    + " FROM Session s "
                    + " WHERE s.parkingId = :parkingId AND EXTRACT(HOUR FROM s.startTime) IN ("
                    + " SELECT peak_hours.peak_hour FROM ( SELECT EXTRACT(HOUR FROM s2.startTime) as peak_hour, "
                    + "count(*) as hour_count "
                    + "   FROM Session s2 WHERE s2.parkingId = :parkingId "
                    + "   GROUP BY EXTRACT(HOUR FROM s2.startTime) ORDER BY hour_count DESC"
                    + " ) as peak_hours"
                    + ") "
                    + " GROUP BY plateNumber, vehicleId "
                    + " ORDER BY session_count DESC",
                Object[].class)
            .setParameter("parkingId", parkingId)
            .getResultList();

    log.info("Result list: {}", resultList);
    if (resultList == null || resultList.isEmpty()) {
      log.warn("No data available for parkingId {}", parkingId);
      return List.of();
    }

    return resultList.stream()
        .map(
            row ->
                new TopVehiclesInPeakHoursDto(
                    (UUID) row[0], (String) row[1], ((Number) row[2]).longValue()))
        .toList();
  }
}
