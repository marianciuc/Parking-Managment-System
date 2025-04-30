package pl.edu.zut.app.parking.parking.specifications;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.parking.entities.AbstractVehicleList;

public class AccessListSpecification {

  public static Specification<AbstractVehicleList> wherePlateNumberEquals(String plateNumber) {
    if (plateNumber == null || plateNumber.isEmpty()) return null;
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("plateNumber"), "%" + plateNumber + "%");
  }

  public static Specification<AbstractVehicleList> whereVehicleIdEquals(UUID vehicleId) {
    if (vehicleId == null) return null;
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("vehicleId"), vehicleId);
  }

  public static Specification<AbstractVehicleList> whereIdEquals(UUID id) {
    if (null == id) return null;
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
  }

  public static Specification<AbstractVehicleList> whereParkingIdEquals(UUID parkingId) {
    if (parkingId == null) return null;
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("parking").get("id"), parkingId);
  }
}
