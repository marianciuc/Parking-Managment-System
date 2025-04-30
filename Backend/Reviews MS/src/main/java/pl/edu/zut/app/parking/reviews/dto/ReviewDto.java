package pl.edu.zut.app.parking.reviews.dto;

import pl.edu.zut.app.parking.reviews.entities.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewDto (
        UUID id,
        String reviewText,
        Integer rating,
        UUID authorId,
        UUID parkingId,
        Review.ModerationStatus moderationStatus,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) {
    public static ReviewDto fromEntity(Review save) {
        return new ReviewDto(
                save.getId(),
                save.getReviewText(),
                save.getRating(),
                save.getAuthorId(),
                save.getParkingId(),
                save.getModerationStatus(),
                save.getCreationDate(),
                save.getModificationDate()
        );
    }
}
