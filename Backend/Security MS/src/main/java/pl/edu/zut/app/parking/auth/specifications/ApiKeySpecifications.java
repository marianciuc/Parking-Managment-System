package pl.edu.zut.app.parking.auth.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.auth.entities.ApiKey;

import java.util.UUID;

public class ApiKeySpecifications {
    public static Specification<ApiKey> withParkingId(UUID parkingId) {
        if (parkingId == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("parkingId"), parkingId);
    }

    public static Specification<ApiKey> withStatus(ApiKey.ApiKeyStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<ApiKey> withIssuedBy(UUID issuedBy) {
        if (issuedBy == null) {
            return null;
        }
        return (root, query, builder) -> builder.equal(root.get("issuedBy"), issuedBy);
    }
}
