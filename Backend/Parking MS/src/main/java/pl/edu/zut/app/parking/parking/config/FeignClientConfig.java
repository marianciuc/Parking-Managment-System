package pl.edu.zut.app.parking.parking.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.zut.app.parking.model.ApiKeyAuthentication;
import pl.edu.zut.app.parking.model.JwtTokenAuthentication;

import java.util.stream.Collectors;

@Slf4j
@Configuration
public class FeignClientConfig {
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-User-Roles";
    private static final String AUTH_HEADER_PARKING_ID = "X-Parking-Id";
    private static final String AUTH_HEADER_SCOPE = "X-Scope";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                log.info("No authentication found in security context");
                return;
            }

            if (authentication.getPrincipal() instanceof JwtTokenAuthentication authenticatedUser) {
                requestTemplate.header(USER_ID_HEADER, authenticatedUser.getId().toString());
                requestTemplate.header(ROLES_HEADER, authenticatedUser.getAuthorities().stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
                log.info("Found JWT token in security context");
            }
            if (authentication.getPrincipal() instanceof ApiKeyAuthentication apiKeyAuthentication) {
                requestTemplate.header(AUTH_HEADER_PARKING_ID, apiKeyAuthentication.getId().toString());
                requestTemplate.header(AUTH_HEADER_SCOPE, apiKeyAuthentication.getAuthorities().stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
        };
    }
}
