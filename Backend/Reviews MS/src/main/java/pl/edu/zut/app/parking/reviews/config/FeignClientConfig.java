package pl.edu.zut.app.parking.reviews.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.zut.app.parking.reviews.security.AuthenticatedUser;
import pl.edu.zut.app.parking.reviews.security.SecurityUtils;

import java.util.stream.Collectors;

@Configuration
public class FeignClientConfig {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-User-Roles";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            AuthenticatedUser authenticatedUser = SecurityUtils.getAuthenticatedUser().orElse(null);
            if (authenticatedUser != null) {
                requestTemplate.header(USER_ID_HEADER, authenticatedUser.getId().toString());
                requestTemplate.header(ROLES_HEADER, authenticatedUser.getAuthorities().stream().map(Object::toString)
                        .collect(Collectors.joining(",")));
            }
        };
    }
}
