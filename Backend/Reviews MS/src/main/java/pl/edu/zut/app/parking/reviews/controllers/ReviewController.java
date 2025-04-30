package pl.edu.zut.app.parking.reviews.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.reviews.dto.ReviewDto;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.services.ReviewsService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewsService reviewService;

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewDto>> searchReviews(
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
            @RequestParam(name = "parkingId", required = false) UUID parkingId,
            @RequestParam(name = "authorId", required = false) UUID authorId

    ) {
        ReviewDto filter = new ReviewDto(null, null, null, authorId, parkingId, null, null, null);
        return ResponseEntity.ok(reviewService.findReviews(page, size, sort, direction, filter));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.createReview(reviewDto));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('DRIVER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reviewId}/report")
    @PreAuthorize("hasAnyRole('DRIVER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> reportReview(@PathVariable UUID reviewId) {
        reviewService.reportReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}/status")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> changeReviewStatus(@PathVariable UUID reviewId, @RequestParam Review.ModerationStatus status) {
        reviewService.changeReviewStatus(reviewId, status);
        return ResponseEntity.ok().build();
    }
}
