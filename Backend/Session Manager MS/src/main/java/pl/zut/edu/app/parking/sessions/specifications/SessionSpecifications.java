package pl.zut.edu.app.parking.sessions.specifications;

import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.zut.edu.app.parking.sessions.dto.filters.SessionFilter;
import pl.zut.edu.app.parking.sessions.entities.Session;

public class SessionSpecifications {

  // Constants for field names to avoid hardcoded strings
  public static final String FIELD_ID = "id";
  public static final String FIELD_PARKING_ID = "parkingId";
  public static final String FIELD_VEHICLE_ID = "vehicleId";
  public static final String FIELD_PLATE_NUMBER = "plateNumber";
  public static final String FIELD_STATUS = "status";
  public static final String FIELD_OWNER_ID = "ownerId";

  public static Specification<Session> byId(UUID id) {
    return (id == null)
        ? (root, query, cb) -> cb.conjunction() // No-op if id is null
        : (root, query, cb) -> cb.equal(root.get(FIELD_ID), id);
  }

  public static Specification<Session> byParkingId(UUID parkingId) {
    return (parkingId == null)
        ? (root, query, cb) -> cb.conjunction() // No-op if parkingId is null
        : (root, query, cb) -> cb.equal(root.get(FIELD_PARKING_ID), parkingId);
  }

  public static Specification<Session> byVehicleId(UUID vehicleId) {
    return (vehicleId == null)
        ? (root, query, cb) -> cb.conjunction() // No-op if vehicleId is null
        : (root, query, cb) -> cb.equal(root.get(FIELD_VEHICLE_ID), vehicleId);
  }

  public static Specification<Session> byPlateNumber(String plateNumber) {
    return (plateNumber == null || plateNumber.isBlank())
        ? (root, query, cb) -> cb.conjunction()
        : (root, query, cb) -> cb.equal(root.get(FIELD_PLATE_NUMBER), plateNumber.trim());
  }

  public static Specification<Session> byStatus(Session.SessionStatus status) {
    return (status == null)
        ? (root, query, cb) -> cb.conjunction()
        : (root, query, cb) -> cb.equal(root.get(FIELD_STATUS), status);
  }

  public static Specification<Session> byOwnerId(UUID ownerId) {
    return (ownerId == null)
        ? (root, query, cb) -> cb.conjunction()
        : (root, query, cb) -> cb.equal(root.get(FIELD_OWNER_ID), ownerId);
  }

  public static Specification<Session> buildSpecifications(SessionFilter filter) {
    Specification<Session> spec = Specification.where(null);

    if (filter.id() != null) {
      spec = spec.and(byId(filter.id()));
    }
    if (filter.parkingId() != null) {
      spec = spec.and(byParkingId(filter.parkingId()));
    }
    if (filter.ownerId() != null) {
      spec = spec.and(byOwnerId(filter.ownerId()));
    }
    if (filter.vehicleId() != null) {
      spec = spec.and(byVehicleId(filter.vehicleId()));
    }
    if (filter.plateNumber() != null && !filter.plateNumber().isBlank()) {
      spec = spec.and(byPlateNumber(filter.plateNumber().trim()));
    }
    if (filter.status() != null) {
      spec = spec.and(byStatus(filter.status()));
    }

    return spec;
  }

  public static Sort.Direction getSortDirection(String direction) {
    return ("DSC".equalsIgnoreCase(direction)) ? Sort.Direction.DESC : Sort.Direction.ASC;
  }
}
