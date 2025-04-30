package pl.edu.zut.app.parking.admin_ms;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.admin_ms.entity.Administrator;

public class AdministratorSpecifications {

  public static Specification<Administrator> whereFirstnameContains(String firstname) {
    return (root, query, cb) -> {
      if (firstname == null) {
        return null;
      }
      return cb.like(root.get("firstname"), "%" + firstname + "%");
    };
  }

  public static Specification<Administrator> whereLastnameContains(String lastname) {
    return (root, query, cb) -> {
      if (lastname == null) {
        return null;
      }
      return cb.like(root.get("lastname"), "%" + lastname + "%");
    };
  }

  public static Specification<Administrator> whereSystemUserIdEquals(UUID systemUserId) {
    return (root, query, cb) -> {
      if (systemUserId == null) {
        return null;
      }
      return cb.equal(root.get("systemUserId"), systemUserId);
    };
  }

  public static Specification<Administrator> build(String firstname, String lastname, UUID systemUserId) {
    return Specification.where(whereFirstnameContains(firstname))
        .and(whereLastnameContains(lastname))
        .and(whereSystemUserIdEquals(systemUserId));
  }
}
