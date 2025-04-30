package pl.edu.zut.app.parking.auth.security.converters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.services.JWTService;


/**
 * Converter to extract and process JWT tokens from HTTP requests.
 */
@Slf4j
public class JWTAuthenticationConverter implements AuthenticationConverter {

    private final JWTService jwtService;

    /**
     * Constructs a JWTAuthenticationConverter with the given deserializers.
     *
     * @param jwtService the service to use for token serialization / deserialization.
     * @see JWTService
     */
    public JWTAuthenticationConverter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Converts the received HTTP request to an {@link Authentication} object based on JWT token.
     *
     * @param request the HttpServletRequest object.
     * @return an Authentication object or null if conversion fails.
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            AuthToken accessAuthToken = this.jwtService.accessTokenStringDeserialize(token);
            if (accessAuthToken != null) {
                log.info("Access token has been found");
                return new PreAuthenticatedAuthenticationToken(accessAuthToken, token);
            }

            AuthToken refreshAuthToken = this.jwtService.refreshTokenStringDeserialize(token);
            if (refreshAuthToken != null) {
                log.info("Refresh token has been found");
                return new PreAuthenticatedAuthenticationToken(refreshAuthToken, token);
            }
        }
        return null;
    }
}
