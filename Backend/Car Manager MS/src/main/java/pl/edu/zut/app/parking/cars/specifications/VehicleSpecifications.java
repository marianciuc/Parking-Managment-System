package pl.edu.zut.app.parking.cars.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.cars.entities.Ownership;
import pl.edu.zut.app.parking.cars.entities.Vehicle;

import java.util.UUID;

public class VehicleSpecifications {

  private VehicleSpecifications() {
    throw new IllegalStateException("Utility class");
  }

  public static Specification<Vehicle> whereBrand(String brand) {
    return (root, query, criteriaBuilder) -> {
      if (brand == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    };
  }

  public static Specification<Vehicle> whereModel(String model) {
    return (root, query, criteriaBuilder) -> {
      if (model == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("model")), "%" + model.toLowerCase() + "%");
    };
  }

  public static Specification<Vehicle> wherePlateNumber(String plateNumber) {
    return (root, query, criteriaBuilder) -> {
      if (plateNumber == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("plateNumber")), "%" + plateNumber.toLowerCase() + "%");
    };
  }

  public static Specification<Vehicle> whereOwnerId(UUID ownerId) {
    return (root, query, criteriaBuilder) -> {
      if (ownerId == null) {
        return criteriaBuilder.conjunction();
      }

      Join<Object, Object> ownershipJoin = root.join("ownership");
      return criteriaBuilder.equal(ownershipJoin.get("ownerId"), ownerId);
    };
  }

  public static Specification<Vehicle> whereOwnerType(Ownership.OwnerType ownerType) {
    return (root, query, criteriaBuilder) -> {
      if (ownerType == null) {
        return criteriaBuilder.conjunction();
      }

      Join<Object, Object> ownershipJoin = root.join("ownership");
      return criteriaBuilder.equal(ownershipJoin.get("ownerType"), ownerType);
    };
  }

  public static Specification<Vehicle> whereIdEquals(UUID id) {
    return (root, query, criteriaBuilder) -> {
      if (id == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get("id"), id);
    };
  }

  public static Specification<Vehicle> whereColor(String color) {
    return (root, query, criteriaBuilder) -> {
      if (color == null) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.like(
          criteriaBuilder.lower(root.get("color")), "%" + color.toLowerCase() + "%");
    };
  }
}
