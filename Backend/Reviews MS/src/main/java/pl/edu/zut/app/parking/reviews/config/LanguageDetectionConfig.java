package pl.edu.zut.app.parking.reviews.config;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class LanguageDetectionConfig {

    @Bean
    public LanguageDetector languageDetector() throws IOException {
        return LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                .build();
    }

    @Bean
    public TextObjectFactory textObjectFactory() {
        return CommonTextObjectFactories.forDetectingOnLargeText();
    }

}
