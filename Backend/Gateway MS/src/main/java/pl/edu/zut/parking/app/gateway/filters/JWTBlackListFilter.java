package pl.edu.zut.parking.app.gateway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.zut.parking.app.gateway.services.JwtService;
import pl.edu.zut.parking.app.gateway.services.TokenBlackListService;

import java.io.IOException;
import java.util.Optional;

/**
 * A filter that checks if a JWT token is blacklisted before allowing the request to proceed.
 */
@Component
@AllArgsConstructor
@Slf4j
public class JWTBlackListFilter extends OncePerRequestFilter {

    private final TokenBlackListService tokenBlackListService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Filtering request: {}", request.getRequestURI());
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Optional<String> token = jwtService.extractBearerToken(authHeader);

        if (token.isPresent() && tokenBlackListService.isTokenBlocked(token.get())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
