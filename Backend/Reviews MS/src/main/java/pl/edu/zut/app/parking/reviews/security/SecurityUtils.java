package pl.edu.zut.app.parking.reviews.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.reviews.exceptions.ForbiddenException;

import java.util.Optional;
import java.util.UUID;

@Component
public class SecurityUtils {

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return an {@link Optional} containing the {@code AuthenticatedUser} if the current
     *         security context holds an authenticated user; otherwise, an empty {@link Optional}.
     */
    public static Optional<AuthenticatedUser> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            return Optional.of((AuthenticatedUser) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    /**
     * Retrieves the unique identifier of the currently authenticated user.
     *
     * @return the {@link UUID} of the authenticated user
     * @throws ForbiddenException if no user is authenticated or the authenticated user lacks the necessary permissions
     */
    public static UUID getAuthenticatedUserId() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new ForbiddenException("Current user is not authenticated or lacks necessary permissions"))
                .getId();
    }
}
