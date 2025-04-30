package pl.edu.zut.app.parking.reviews.services.impl;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.services.BannedWordsService;
import pl.edu.zut.app.parking.reviews.services.LanguageDetectionService;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ModerationServiceImplTest {

    @Mock
    private LanguageDetectionService languageDetectionService;

    @Mock
    private BannedWordsService bannedWordsService;

    @InjectMocks
    private ModerationServiceImpl moderationService;

    private Faker faker;
    private Review review;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        review = Review.builder()
                .id(UUID.randomUUID())
                .reviewText(faker.lorem().paragraph())
                .rating(faker.number().numberBetween(1, 5))
                .authorId(UUID.randomUUID())
                .parkingId(UUID.randomUUID())
                .moderationStatus(Review.ModerationStatus.PENDING)
                .build();
    }

    @Test
    @DisplayName("Should return REJECTED when review is null")
    void processSystemModeration_NullReview_ReturnsRejected() {
        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(null);

        // Assert
        assertThat(result).isEqualTo(Review.ModerationStatus.REJECTED);
    }

    @Test
    @DisplayName("Should return REJECTED when review text is null")
    void processSystemModeration_NullReviewText_ReturnsRejected() {
        // Arrange
        review.setReviewText(null);

        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(review);

        // Assert
        assertThat(result).isEqualTo(Review.ModerationStatus.REJECTED);
    }

    @Test
    @DisplayName("Should return PENDING when language detection fails")
    void processSystemModeration_UnknownLanguage_ReturnsPending() {
        // Arrange
        when(languageDetectionService.detectLanguage(anyString())).thenReturn("unknown");

        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(review);

        // Assert
        assertThat(result).isEqualTo(Review.ModerationStatus.PENDING);
    }

    @Test
    @DisplayName("Should return REJECTED when banned words are detected")
    void processSystemModeration_ContainsBannedWords_ReturnsRejected() {
        // Arrange
        when(languageDetectionService.detectLanguage(anyString())).thenReturn("en");
        when(bannedWordsService.containsBannedWords(anyString(), anyString())).thenReturn(true);

        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(review);

        // Assert
        assertThat(result).isEqualTo(Review.ModerationStatus.REJECTED);
    }

    @Test
    @DisplayName("Should return ACCEPTED for valid review without banned words")
    void processSystemModeration_ValidReview_ReturnsAccepted() {
        // Arrange
        when(languageDetectionService.detectLanguage(anyString())).thenReturn("en");
        when(bannedWordsService.containsBannedWords(anyString(), anyString())).thenReturn(false);

        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(review);

        // Assert
        assertThat(result).isEqualTo(Review.ModerationStatus.ACCEPTED);
    }

    @ParameterizedTest
    @MethodSource("provideLanguagesAndBannedWordsScenarios")
    @DisplayName("Should handle different language and banned words scenarios")
    void processSystemModeration_VariousScenarios(String language, boolean hasBannedWords, Review.ModerationStatus expectedStatus) {
        // Arrange
        when(languageDetectionService.detectLanguage(any())).thenReturn(language);
        when(bannedWordsService.containsBannedWords(anyString(), anyString())).thenReturn(hasBannedWords);

        // Act
        Review.ModerationStatus result = moderationService.processSystemModeration(review);

        // Assert
        assertThat(result).isEqualTo(expectedStatus);
    }

    private static Stream<Arguments> provideLanguagesAndBannedWordsScenarios() {
        return Stream.of(
                Arguments.of("en", false, Review.ModerationStatus.ACCEPTED),
                Arguments.of("pl", false, Review.ModerationStatus.ACCEPTED),
                Arguments.of("unknown", false, Review.ModerationStatus.PENDING),
                Arguments.of("en", true, Review.ModerationStatus.REJECTED),
                Arguments.of("pl", true, Review.ModerationStatus.REJECTED)
        );
    }
}