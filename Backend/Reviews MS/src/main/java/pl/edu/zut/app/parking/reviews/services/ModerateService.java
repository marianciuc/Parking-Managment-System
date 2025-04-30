package pl.edu.zut.app.parking.reviews.services;

import pl.edu.zut.app.parking.reviews.entities.Review;

/**
 * Interface for handling system-level moderation of reviews. It provides functionality
 * to apply automated moderation rules, including language detection and screening for
 * prohibited content, to determine the review's moderation status.
 */
public interface ModerateService {

    /**
     * Processes the moderation of a given review by applying system-defined moderation rules.
     * This process may include checks such as language detection, banned word identification,
     * or other business logic to determine the appropriate moderation status.
     *
     * @param review the review entity containing details such as review text, rating, author ID,
     *               parking ID, and its current moderation status
     * @return the updated moderation status of the review after processing ACCEPTED, REJECTED as determined by the
     * system. If the review is null or has no text, the status will be REJECTED. If language detection fails, the
     * status will be PENDING. If banned words are detected, the status will be REJECTED.
     */
    Review.ModerationStatus processSystemModeration(Review review);
}
