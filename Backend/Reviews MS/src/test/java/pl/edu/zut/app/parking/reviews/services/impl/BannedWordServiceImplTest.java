package pl.edu.zut.app.parking.reviews.services.impl;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BannedWordServiceImplTest {

    @InjectMocks
    private BannedWordServiceImpl bannedWordService;

    @Mock
    private ClassLoader classLoader;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        Map<String, String> sources = new HashMap<>();
        sources.put("en", "banned-words/en");
        sources.put("pl", "banned-words/pl");
        ReflectionTestUtils.setField(bannedWordService, "sources", sources);
    }

    @ParameterizedTest
    @CsvSource({
            "This text contains bad words, en, true",
            "This is a clean text, en, false",
            "Text with unknown language, unknown, false"
    })
    void containsBannedWords_ShouldCheckTextCorrectly(String text, String language, boolean expected) {
        // Arrange
        Map<String, Set<String>> bannedWordsMap = new HashMap<>();
        bannedWordsMap.put("en", Set.of("bad", "worse", "evil"));
        ReflectionTestUtils.setField(bannedWordService, "bannedWordsMap", bannedWordsMap);

        // Act
        boolean result = bannedWordService.containsBannedWords(text, language);

        // Assert
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void containsBannedWords_ShouldHandleEmptyInput(String text) {
        // Act
        boolean result = bannedWordService.containsBannedWords(text, "en");

        // Assert
        assertFalse(result);
    }

    @Test
    void init_ShouldThrowException_WhenSourcesAreEmpty() {
        ReflectionTestUtils.setField(bannedWordService, "sources", new HashMap<>());

        // Act & Assert
        assertThrows(IllegalStateException.class, bannedWordService::init);
    }
}