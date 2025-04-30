package pl.edu.zut.app.parking.reviews.specifications;

import org.springframework.data.jpa.domain.Specification;
import pl.edu.zut.app.parking.reviews.entities.Review;

import java.util.UUID;

public class ReviewsSpecifications {
    public static Specification<Review> hasParkingId(UUID parkingId) {
        if (parkingId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parkingId"), parkingId);
    }

    public static Specification<Review> hasAuthorId(UUID authorId) {
        if (authorId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("authorId"), authorId);
    }

    public static Specification<Review> hasModerationStatus(Review.ModerationStatus moderationStatus) {
        final Review.ModerationStatus fallbackStatus = moderationStatus == null ? Review.ModerationStatus.ACCEPTED : moderationStatus;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("moderationStatus"), fallbackStatus);
    }
}
