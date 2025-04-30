package pl.edu.zut.app.parking.reviews.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.zut.app.parking.reviews.client.ParkingClient;
import pl.edu.zut.app.parking.reviews.dto.ReviewDto;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.exceptions.ParkingNotFoundException;
import pl.edu.zut.app.parking.reviews.kafka.ReviewsUpdateMessageProducer;
import pl.edu.zut.app.parking.reviews.repositories.ReviewsRepository;
import pl.edu.zut.app.parking.reviews.security.AuthenticatedUser;
import pl.edu.zut.app.parking.reviews.services.ModerateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewsServiceImplTest {
    private static final int DEFAULT_RATING = 4;

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private ParkingClient parkingClient;

    @Mock
    private ModerateService moderateService;

    @Mock
    private ReviewsUpdateMessageProducer messageProducer;

    @InjectMocks
    private ReviewsServiceImpl reviewsService;

    private UUID testUserId;
    private UUID testParkingId;
    private ReviewDto testReviewDto;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testParkingId = UUID.randomUUID();
        setupSecurityContext();
        setupTestData();
    }

    private void setupSecurityContext() {
        AuthenticatedUser user = new AuthenticatedUser(testUserId.toString(), "USER");
        SecurityContextHolder.getContext()
                .setAuthentication(new TestingAuthenticationToken(user, null, "USER"));
    }

    private void setupTestData() {
        testReviewDto = new ReviewDto(
                UUID.randomUUID(),
                "Test review",
                DEFAULT_RATING,
                testUserId,
                testParkingId,
                Review.ModerationStatus.PENDING,
                null,
                null
        );

        testReview = Review.builder()
                .id(testReviewDto.id())
                .reviewText(testReviewDto.reviewText())
                .rating(testReviewDto.rating())
                .authorId(testReviewDto.authorId())
                .parkingId(testReviewDto.parkingId())
                .moderationStatus(testReviewDto.moderationStatus())
                .build();
    }

    @Nested
    class CreateReviewTests {
        @Test
        void shouldCreateReviewSuccessfully() {
            // Arrange
            when(parkingClient.parkingExists(testParkingId)).thenReturn(ResponseEntity.ok(true));
            when(reviewsRepository.findByAuthorIdAndParkingId(testUserId, testParkingId))
                    .thenReturn(Optional.empty());
            when(moderateService.processSystemModeration(any())).thenReturn(Review.ModerationStatus.PENDING);
            when(reviewsRepository.save(any())).thenReturn(testReview);

            // Act
            ReviewDto result = reviewsService.createReview(testReviewDto);

            // Assert
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(testReviewDto.reviewText(), result.reviewText()),
                    () -> assertEquals(testReviewDto.rating(), result.rating()),
                    () -> verify(messageProducer).sendNewReviewMessage(testParkingId, testReviewDto.rating())
            );
        }

        @Test
        void shouldThrowExceptionWhenParkingNotFound() {
            // Arrange
            when(parkingClient.parkingExists(testParkingId)).thenReturn(ResponseEntity.ok(false));

            // Act & Assert
            assertThrows(ParkingNotFoundException.class,
                    () -> reviewsService.createReview(testReviewDto));
        }
    }

    @Nested
    class FindReviewsTests {
        @ParameterizedTest
        @ValueSource(ints = {0, 1, 5})
        void shouldReturnCorrectNumberOfReviews(int size) {
            // Arrange
            List<Review> reviews = createReviewList(size);
            Page<Review> page = new PageImpl<>(reviews);
            when(reviewsRepository.findAll(any(Specification.class), any(PageRequest.class)))
                    .thenReturn(page);

            // Act
            Page<ReviewDto> result = reviewsService.findReviews(0, 10, "id", "ASC", testReviewDto);

            // Assert
            assertEquals(size, result.getContent().size());
        }
    }

    private List<Review> createReviewList(int size) {
        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            reviews.add(testReview);
        }
        return reviews;
    }
}