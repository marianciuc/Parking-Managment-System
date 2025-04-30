package pl.edu.zut.app.parking.reviews.services.impl;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.text.TextObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.reviews.services.LanguageDetectionService;

import com.optimaize.langdetect.text.TextObjectFactory;

import java.util.Optional;


@Service
@Slf4j
public class LanguageDetectionServiceImpl implements LanguageDetectionService {

    private final LanguageDetector languageDetector;
    private final TextObjectFactory textObjectFactory;

    public LanguageDetectionServiceImpl(LanguageDetector languageDetector, TextObjectFactory textObjectFactory) {
        this.languageDetector = languageDetector;
        this.textObjectFactory = textObjectFactory;
    }

    @Override
    public String detectLanguage(String text) {
        if (text == null || text.isBlank()) {
            return "unknown";
        }

        TextObject textObject = textObjectFactory.forText(text);

        try {
            Optional<LdLocale> detectedLanguage = Optional.ofNullable(languageDetector.detect(textObject))
                    .map(opt -> com.google.common.base.Optional.toJavaUtil(opt))
                    .orElse(Optional.empty());
            return detectedLanguage.map(LdLocale::getLanguage).orElse("unknown");
        } catch (Exception e) {
            log.error("Error detecting language for text: {}", text, e);
            return "unknown";
        }
    }

}
