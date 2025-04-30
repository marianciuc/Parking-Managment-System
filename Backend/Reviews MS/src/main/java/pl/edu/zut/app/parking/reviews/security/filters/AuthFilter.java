package pl.edu.zut.app.parking.reviews.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.zut.app.parking.reviews.security.AuthenticatedUser;

import java.io.IOException;

/**
 * Filter that processes JWT authentication from the HTTP Authorization header.
 */
public class AuthFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String ROLES_HEADER = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader(USER_ID_HEADER);
        String rolesHeader = request.getHeader(ROLES_HEADER);

        // TODO: add gateway secured key validation

        if (userIdHeader != null || rolesHeader != null) {
            createAuthentication(userIdHeader, rolesHeader);
        }

        filterChain.doFilter(request, response);
    }

    private void createAuthentication(String userId, String roles) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, roles);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, authenticatedUser.getId(), authenticatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
