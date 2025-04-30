package pl.edu.zut.app.parking.auth.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.UserType;

public class UserSpecifications {
    public static Specification<User> withEmail(String email) {
        if (email == null) {
            return null;
        }
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> withRecordStatus(User.RecordStatus recordStatus) {
        if (recordStatus == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("recordStatus"), recordStatus);
    }

    public static Specification<User> withUserType(UserType userType) {
        if (userType == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("userType"), userType);
    }
}
