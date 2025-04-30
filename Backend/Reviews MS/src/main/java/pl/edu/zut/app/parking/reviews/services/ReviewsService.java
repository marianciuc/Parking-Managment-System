package pl.edu.zut.app.parking.reviews.services;

import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.reviews.dto.ReviewDto;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.exceptions.ForbiddenException;
import pl.edu.zut.app.parking.reviews.exceptions.IllegalReviewOperation;
import pl.edu.zut.app.parking.reviews.exceptions.ReviewNotFoundException;

import java.util.UUID;

/**
 * Provides a service for managing reviews associated with parking locations.
 * This includes creating, retrieving, updating, and deleting reviews.
 */
public interface ReviewsService {

    /**
     * Creates a new review based on the provided ReviewDto.
     *
     * @param reviewDto the data transfer object containing the review's details
     * @return the created review as a ReviewDto
     * @throws pl.edu.zut.app.parking.reviews.exceptions.ParkingNotFoundException if the parking with the provided ID does not exist
     * @throws IllegalReviewOperation   if a review for the specified parking location already exists
     */
    ReviewDto createReview(ReviewDto reviewDto);

    /**
     * Retrieves a paginated list of reviews based on specified criteria.
     *
     * @param page      the page number to retrieve (0-based indexing)
     * @param size      the number of reviews per page
     * @param sort      the field by which the reviews should be sorted
     * @param direction the direction of sorting, either ascending or descending
     * @param filter    an object containing filters for the reviews, such as rating, authorId, parkingId, or moderationStatus
     * @return a page containing ReviewDto objects that match the specified criteria
     */
    Page<ReviewDto> findReviews(int page, int size, String sort, String direction, ReviewDto filter);

    /**
     * Deletes the review with the specified review ID.
     *
     * @param reviewId the unique identifier of the review to be deleted
     * @throws pl.edu.zut.app.parking.reviews.exceptions.ReviewNotFoundException if the review with the provided ID does not exist
     * @throws ForbiddenException if the user is not authorized to delete the review
     */
    void deleteReview(UUID reviewId);

    /**
     * Changes the moderation status of the review with the specified review ID. The allowed statuses are ACCEPTED and REJECTED.
     *
     * @param reviewId the unique identifier of the review to be updated
     * @param status   the new moderation status to be set
     * @throws ReviewNotFoundException if the review with the provided ID does not exist
     * @throws IllegalReviewOperation  if the status is set to REPORTED or PENDING
     */
    void changeReviewStatus(UUID reviewId, Review.ModerationStatus status);

    /**
     * Reports a review based on its unique identifier.
     * This action may be used to mark the review for further moderation or review process.
     *
     * @param reviewId the unique identifier of the review to be reported
     * @throws pl.edu.zut.app.parking.reviews.exceptions.ReviewNotFoundException if the review with the provided ID does not exist
     */
    void reportReview(UUID reviewId);
}
