package pl.edu.zut.app.parking.auth.security.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;
import pl.edu.zut.app.parking.auth.dto.req.Credentials;

import java.io.IOException;

@Slf4j
public class UsernamePasswordAuthenticationConverter implements AuthenticationConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert HttpServletRequest to UsernamePasswordAuthenticationToken
     * @param request - HttpServletRequest
     * @return UsernamePasswordAuthenticationToken authentication if credentials are valid, null otherwise
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        try {
            Credentials credentials = objectMapper.readValue(request.getInputStream(), Credentials.class);
            if (credentials != null &&
                    StringUtils.hasText(credentials.email()) &&
                    StringUtils.hasText(credentials.password())) {
                return new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());
            }
        } catch (IOException e) {
            log.error("Error parsing credentials", e);
            return null;
        }
        return null;
    }
}
