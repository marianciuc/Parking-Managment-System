package pl.edu.zut.app.parking.reviews.services.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.reviews.exceptions.BannedWordsLoadingException;
import pl.edu.zut.app.parking.reviews.services.BannedWordsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConfigurationProperties(prefix = "banned-words")
public class BannedWordServiceImpl implements BannedWordsService {


    private final Map<String, Set<String>> bannedWordsMap = new HashMap<>();

    private Map<String, String> sources = new HashMap<>();

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }

    @PostConstruct
    public void init() {
        if (sources.isEmpty()) {
            throw new IllegalStateException("Banned words sources configuration is missing");
        }

        sources.forEach((language, filePath) -> {
            try {
                Set<String> words = loadBannedWordsFromFile(filePath);
                if (!words.isEmpty()) {
                    bannedWordsMap.put(language, words);
                    log.info("Successfully loaded {} words for {}", words.size(), language);
                }
            } catch (BannedWordsLoadingException e) {
                log.error("Error loading banned words for language {}: {}", language, e.getMessage());
            }
        });

    }


    private Set<String> loadBannedWordsFromFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            log.warn("Invalid file path provided. Returning empty banned word set.");
            return Set.of();
        }

        Resource resource = new ClassPathResource(filePath); // Загружаем ресурс

        if (!resource.exists()) {
            log.error("File not found for path: {}", filePath);
            throw new BannedWordsLoadingException("File not found for path: " + filePath, null);
        }

        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.startsWith("#") && !line.isEmpty())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
            throw new BannedWordsLoadingException("Error reading file: " + filePath, e);
        }
    }



    private Set<String> getBannedWordsByLanguage(String language) {
        return bannedWordsMap.getOrDefault(language, Set.of());
    }


    @Override
    public boolean containsBannedWords(String text, String language) {
        if (text == null || text.trim().isEmpty() || language == null) {
            return false;
        }
        return getBannedWordsByLanguage(language).stream()
                .anyMatch(text::contains);

    }
}
