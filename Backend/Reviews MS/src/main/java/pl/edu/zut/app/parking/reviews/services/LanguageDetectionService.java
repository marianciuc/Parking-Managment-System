package pl.edu.zut.app.parking.reviews.services;

/**
 * Service interface for detecting the language of a given text.
 * Provides functionality to identify the language of input strings
 * and returns the corresponding language code in iso-639-1 format.
 */
public interface LanguageDetectionService {

    /**
     * Detects the language of the provided text.
     *
     * @param text the input text whose language needs to be identified
     * @return the detected language code as a String (e.g., "en" for English, "pl" for Polish) in iso-639-1 format
     * or "unknown" if the language could not be detected
     */
    String detectLanguage(String text);
}
