package pl.edu.zut.app.parking.parking.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.parking.entities.Tag;

import java.util.UUID;

public class TagSpecifications {

    public static Specification<Tag> whereNameContains(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Tag> whereIdEquals(UUID id) {
        if (id == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
}
