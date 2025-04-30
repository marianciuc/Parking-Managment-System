package pl.edu.zut.app.parking.auth.security.utils;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUserDetails;

/**
 * Utility class for handling security-related operations.
 */
public class SecurityUtils {

    /**
     * Extracts the authenticated user from the current security context.
     * @return the authenticated user
     * @see pl.edu.zut.app.parking.auth.security.AuthenticatedUserDetails
     * @throws AccessDeniedException if the user is not authenticated or has invalid principal
     */
    public static AuthenticatedUserDetails extractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof PreAuthenticatedAuthenticationToken && authentication.getPrincipal() instanceof AuthenticatedUserDetails userDetails) {
            return userDetails;
        }
        else throw new AccessDeniedException("Access denied: User is not authenticated or has invalid principal.");
    }
}
