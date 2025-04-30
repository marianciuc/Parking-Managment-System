package pl.edu.zut.app.parking.reviews.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.reviews.client.ParkingClient;
import pl.edu.zut.app.parking.reviews.dto.ReviewDto;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.exceptions.*;
import pl.edu.zut.app.parking.reviews.kafka.ReviewsUpdateMessageProducer;
import pl.edu.zut.app.parking.reviews.repositories.ReviewsRepository;
import pl.edu.zut.app.parking.reviews.security.SecurityUtils;
import pl.edu.zut.app.parking.reviews.services.ModerateService;
import pl.edu.zut.app.parking.reviews.services.ReviewsService;
import pl.edu.zut.app.parking.reviews.specifications.ReviewsSpecifications;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final ParkingClient parkingClient;
    private final ReviewsUpdateMessageProducer reviewsUpdateMessageProducer;
    private final ModerateService moderateService;

    /**
     * Creates a new review based on the provided ReviewDto.
     *
     * @param reviewDto the data transfer object containing the review's details
     * @return the created review as a ReviewDto
     * @throws ParkingNotFoundException if the parking with the provided ID does not exist
     * @throws IllegalReviewOperation   if a review for the specified parking location already exists
     */
    @Override
    public ReviewDto createReview(ReviewDto reviewDto) {

        ResponseEntity<Boolean> parkingExists = parkingClient.parkingExists(reviewDto.parkingId());

        if (parkingExists.getStatusCode().isError()) {
            throw new BusinessException("Parking with ID " + reviewDto.parkingId() + " not found");
        } else if (Boolean.FALSE.equals(parkingExists.getBody())) {
            throw new ParkingNotFoundException("Parking with ID " + reviewDto.parkingId() + " not found");
        }

        reviewsRepository.findByAuthorIdAndParkingId(reviewDto.authorId(), reviewDto.parkingId())
                .ifPresent(r -> {
                    throw new IllegalReviewOperation("Review for parking with ID " + reviewDto.parkingId() + " already exists");
                });

        UUID authorId = SecurityUtils.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenException("User not authenticated"))
                .getId();

        Review review = Review.builder()
                .reviewText(reviewDto.reviewText())
                .rating(reviewDto.rating())
                .authorId(authorId)
                .moderationStatus(Review.ModerationStatus.PENDING)
                .parkingId(reviewDto.parkingId())
                .recordStatus(Review.RecordStatus.ACTIVE)
                .build();

        review = reviewsRepository.save(review);

        Review.ModerationStatus moderationStatus = moderateService.processSystemModeration(review);
        review.setModerationStatus(moderationStatus);

        reviewsRepository.save(review);

        reviewsUpdateMessageProducer.sendNewReviewMessage(reviewDto.parkingId(), reviewDto.rating());
        return ReviewDto.fromEntity(review);
    }

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
    @Override
    public Page<ReviewDto> findReviews(int page, int size, String sort, String direction, ReviewDto filter) {
        Specification<Review> specification = Specification.where(ReviewsSpecifications.hasAuthorId(filter.authorId()))
                .and(ReviewsSpecifications.hasParkingId(filter.parkingId()))
                .and(ReviewsSpecifications.hasModerationStatus(filter.moderationStatus()));

        return reviewsRepository.findAll(specification, PageRequest.of(page, size,
                        Sort.by(Sort.Direction.fromString(direction), sort)))
                .map(ReviewDto::fromEntity);
    }

    /**
     * Deletes the review with the specified review ID.
     *
     * @param reviewId the unique identifier of the review to be deleted
     * @throws ReviewNotFoundException if the review with the provided ID does not exist
     * @throws ForbiddenException      if the user is not authorized to delete the review
     */
    @Override
    public void deleteReview(UUID reviewId) {
        Review review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + reviewId + " not found"));

        var authenticatedUser = SecurityUtils.getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenException("User not authenticated"));

        UUID requesterId = authenticatedUser.getId();
        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        if (!requesterId.equals(review.getAuthorId()) && !isAdmin) {
            throw new ForbiddenException("User is not the author of the review and is not an admin");
        }

        reviewsUpdateMessageProducer.sendDeleteReviewMessage(review.getParkingId(), review.getRating());
        reviewsRepository.delete(review);
    }

    /**
     * Changes the moderation status of the review with the specified review ID. The allowed statuses are ACCEPTED and REJECTED.
     *
     * @param reviewId the unique identifier of the review to be updated
     * @param status   the new moderation status to be set
     * @throws ReviewNotFoundException if the review with the provided ID does not exist
     * @throws IllegalReviewOperation  if the status is set to REPORTED or PENDING
     */
    @Override
    public void changeReviewStatus(UUID reviewId, Review.ModerationStatus status) {
        Review review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + reviewId + " not found"));
        if (status == Review.ModerationStatus.REPORTED || status == Review.ModerationStatus.PENDING) {
            throw new IllegalReviewOperation("Cannot set review status to " + status);
        }
        review.setModerationStatus(status);
        reviewsRepository.save(review);
    }

    /**
     * Reports a review based on its unique identifier.
     * This action may be used to mark the review for further moderation or review process.
     *
     * @param reviewId the unique identifier of the review to be reported
     * @throws ReviewNotFoundException if the review with the provided ID does not exist
     */
    @Override
    public void reportReview(UUID reviewId) {
        log.info("Reporting review with ID {}", reviewId);
        Review review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + reviewId + " not found"));
        review.setModerationStatus(Review.ModerationStatus.REPORTED);
        reviewsRepository.save(review);
    }
}
