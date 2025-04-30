package pl.edu.zut.app.parking.reviews.services.impl;

import com.github.javafaker.Faker;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import com.google.common.base.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LanguageDetectionServiceImplTest {

    @Mock
    private LanguageDetector languageDetector;

    @Mock
    private TextObjectFactory textObjectFactory;

    @Mock
    private TextObject textObject;

    @InjectMocks
    private LanguageDetectionServiceImpl languageDetectionService;

    private final Faker faker = new Faker();

    @Test
    @DisplayName("Should detect English language successfully")
    void shouldDetectEnglishLanguage() {
        // Arrange
        String inputText = "Hello, world!";
        String expectedLanguage = "en";

        when(textObjectFactory.forText(inputText)).thenReturn(textObject);
        when(languageDetector.detect(textObject)).thenReturn(Optional.of(LdLocale.fromString(expectedLanguage)));



        // Act
        String detectedLanguage = languageDetectionService.detectLanguage(inputText);

        // Assert
        assertEquals(expectedLanguage, detectedLanguage);
        verify(textObjectFactory).forText(inputText);
        verify(languageDetector).detect(textObject);
    }

    @ParameterizedTest
    @CsvSource({
            "pl, To po polsku",
            "de, Dies ist ein deutscher Text",
            "en, This is an English text"
    })
    @DisplayName("Should detect different languages correctly")
    void shouldDetectDifferentLanguages(String expectedLanguage, String text) {
        TextObject textObjectMock = mock(TextObject.class);
        when(textObjectFactory.forText(text)).thenReturn(textObjectMock);
        when(languageDetector.detect(textObjectMock)).thenReturn(Optional.of(LdLocale.fromString(expectedLanguage)));

        // Act
        String result = languageDetectionService.detectLanguage(text);

        // Assert
        assertThat(result).isEqualTo(expectedLanguage);
        verify(textObjectFactory).forText(text);
        verify(languageDetector).detect(textObjectMock);
    }



    @Test
    @DisplayName("Should return unknown when language detection fails")
    void shouldReturnUnknownWhenDetectionFails() {
        // Arrange
        String text = faker.lorem().sentence();
        doReturn(Optional.absent()).when(languageDetector).detect(textObject);
        when(textObjectFactory.forText(text)).thenReturn(textObject);

        // Act
        String detectedLanguage = languageDetectionService.detectLanguage(text);

        // Assert
        assertThat(detectedLanguage).isEqualTo("unknown");
        verify(textObjectFactory).forText(text); // Ensure TextObject creation is verified
        verify(languageDetector).detect(textObject); // Ensure detector was called
    }


    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should return unknown for null or empty text")
    void detectLanguage_NullOrEmptyText_ReturnsUnknown(String text) {
        // Act
        String result = languageDetectionService.detectLanguage(text);

        // Assert
        assertEquals("unknown", result);
        verifyNoInteractions(textObjectFactory, languageDetector);
    }

    @Test
    @DisplayName("Should handle language detector throwing exception")
    void shouldHandleLanguageDetectorException() {
        // Arrange
        String text = faker.lorem().sentence();
        when(textObjectFactory.forText(text)).thenReturn(textObject);
        when(languageDetector.detect(textObject)).thenThrow(new RuntimeException("Detection failed"));

        // Act
        String detectedLanguage = languageDetectionService.detectLanguage(text);

        // Assert
        assertThat(detectedLanguage).isEqualTo("unknown");
    }
}