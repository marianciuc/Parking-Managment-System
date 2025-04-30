package pl.edu.zut.parking.app.gateway.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cors.allowed")
@Slf4j
public class GatewayCorsConfig {

    private List<String> origin;
    private List<String> methods;
    private List<String> headers;

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        log.info("Configuring allowed origins: {}", origin);
        this.origin = origin;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        log.info("Configuring allowed methods: {}", methods);
        this.methods = methods;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        log.info("Configuring allowed headers: {}", headers);
        this.headers = headers;
    }


    @Bean
    public CorsWebFilter corsWebFilter() {
        log.info("Creating CorsWebFilter with origins: {}, methods: {}, headers: {}", origin, methods, headers);
        if (origin == null || methods == null || headers == null) {
            throw new IllegalStateException("cors.allowed.* properties are not set");
        }

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origin);
        config.setAllowedMethods(methods);
        config.setAllowedHeaders(headers);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}