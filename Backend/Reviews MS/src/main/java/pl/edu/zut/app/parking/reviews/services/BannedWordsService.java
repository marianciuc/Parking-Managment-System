package pl.edu.zut.app.parking.reviews.services;

/**
 * Service interface for handling actions related to detecting and managing banned words in text.
 * This service is typically used to check content and prevent the use of restricted or offensive language
 * based on a specific language context.
 */
public interface BannedWordsService {

    /**
     * Checks if the given text contains banned words based on the specified language.
     *
     * @param text the input text to check for banned words
     * @param language the language code (in iso-639-1 format) used for checking banned words
     * @return true if the text contains banned words, false otherwise
     */
    boolean containsBannedWords(String text, String language);
}
