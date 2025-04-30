package pl.edu.zut.app.parking.reviews.controllers;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.edu.zut.app.parking.reviews.dto.ReviewDto;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.services.ReviewsService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {
    @Mock
    private ReviewsService reviewsService;

    @InjectMocks
    private ReviewController reviewController;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
    }

    @Test
    @DisplayName("Should search reviews successfully")
    void testSearchReviews_positiveCase() {
        ReviewDto filter = new ReviewDto(null, null, null, null, null, null, null, null);
        int page = 0, size = 10;
        String sort = "date", direction = "desc";
        Page<ReviewDto> mockPage = new PageImpl<>(List.of(generateReviewDto(), generateReviewDto()));
        when(reviewsService.findReviews(page, size, sort, direction, filter)).thenReturn(mockPage);

        // Act
        ResponseEntity<Page<ReviewDto>> response = reviewController.searchReviews(size, page, sort, direction,
                null, null);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(2);
        verify(reviewsService, times(1)).findReviews(page, size, sort, direction, filter);
    }

    @Test
    @DisplayName("Should create review successfully")
    void testCreateReview_positiveCase() {
        // Arrange
        ReviewDto inputReviewDto = generateReviewDto();
        ReviewDto returnedReviewDto = generateReviewDto();
        when(reviewsService.createReview(inputReviewDto)).thenReturn(returnedReviewDto);

        // Act
        ResponseEntity<ReviewDto> response = reviewController.createReview(inputReviewDto);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(returnedReviewDto);
        verify(reviewsService, times(1)).createReview(inputReviewDto);
    }

    @Test
    @DisplayName("Should delete review successfully")
    void testDeleteReview_positiveCase() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        doNothing().when(reviewsService).deleteReview(reviewId);

        // Act
        ResponseEntity<Void> response = reviewController.deleteReview(reviewId);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(reviewsService, times(1)).deleteReview(reviewId);
    }

    @Test
    @DisplayName("Should report review successfully")
    void testReportReview_positiveCase() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        doNothing().when(reviewsService).reportReview(reviewId);

        // Act
        ResponseEntity<Void> response = reviewController.reportReview(reviewId);

        // Assert
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(reviewsService, times(1)).reportReview(reviewId);
    }

    @ParameterizedTest
    @CsvSource({
            "ACCEPTED",
            "REJECTED"
    })
    @DisplayName("Should change review status successfully")
    void testChangeReviewStatus_positiveCase(Review.ModerationStatus status) {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        doNothing().when(reviewsService).changeReviewStatus(reviewId, status);

        // Act
        ResponseEntity<Void> response = reviewController.changeReviewStatus(reviewId, status);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reviewsService).changeReviewStatus(reviewId, status);

    }

    private ReviewDto generateReviewDto() {
        return new ReviewDto(
                UUID.randomUUID(),
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 5),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Review.ModerationStatus.PENDING,
                null,
                null
        );
    }

}