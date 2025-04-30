package pl.zut.edu.app.parking.sessions.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.zut.app.parking.model.ApiKeyAuthentication;
import pl.edu.zut.app.parking.model.JwtTokenAuthentication;

import java.util.stream.Collectors;

// TODO: add that code to lib

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
                return;
            }

            if (authentication.getPrincipal() instanceof JwtTokenAuthentication authenticatedUser) {
                requestTemplate.header(USER_ID_HEADER, authenticatedUser.getId().toString());
                requestTemplate.header(ROLES_HEADER, authenticatedUser.getAuthorities().stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
            if (authentication.getPrincipal() instanceof ApiKeyAuthentication apiKeyAuthentication) {
                requestTemplate.header(AUTH_HEADER_PARKING_ID, apiKeyAuthentication.getId().toString());
                requestTemplate.header(AUTH_HEADER_SCOPE, apiKeyAuthentication.getAuthorities().stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
        };
    }
}
