package pl.edu.zut.app.parking.reviews.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.reviews.entities.Review;
import pl.edu.zut.app.parking.reviews.services.BannedWordsService;
import pl.edu.zut.app.parking.reviews.services.LanguageDetectionService;
import pl.edu.zut.app.parking.reviews.services.ModerateService;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerateService {

    private final LanguageDetectionService languageDetectorService;
    private final BannedWordsService bannedWordsService;


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
    @Override
    public Review.ModerationStatus processSystemModeration(Review review) {
        if (Objects.isNull(review) || Objects.isNull(review.getReviewText())) {
            log.warn("Review is null or has no text");
            return Review.ModerationStatus.REJECTED;
        }

        String language = languageDetectorService.detectLanguage(review.getReviewText());
        if ("unknown".equals(language)) {
            log.warn("Language detection failed for review ID: {}", review.getId());
            return Review.ModerationStatus.PENDING;
        }
        log.info("Language automatically detected as: {}", language);


        if (bannedWordsService.containsBannedWords(review.getReviewText(), language)) {
            log.info("Banned words detected in review ID: {}", review.getId());
            return Review.ModerationStatus.REJECTED;
        }

        return Review.ModerationStatus.ACCEPTED;
    }
}
