package pl.edu.zut.app.parking.reviews.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder
@RequiredArgsConstructor
@Table(name = "reviews")
public class Review extends AbstractBaseEntity {

    @Column(nullable = false, name = "review_text")
    private String reviewText;

    @Column(nullable = false, name = "rating")
    private int rating;

    @Column(nullable = false, name = "author_id")
    private UUID authorId;

    @Column(nullable = false, name = "parking_id")
    private UUID parkingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status")
    private ModerationStatus moderationStatus;

    public enum ModerationStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        REPORTED
    }
}
