package pl.edu.zut.app.parking.auth.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * Custom implementation of {@link AuthenticationManager} that iterates through a list of {@link AuthenticationProvider}s
 * to find a suitable provider for the given authentication request.
 */
@Slf4j
public class ProviderManager implements AuthenticationManager {

    private final List<AuthenticationProvider> providers;

    public ProviderManager(List<AuthenticationProvider> providers) {
        this.providers = List.copyOf(providers);
    }

    /**
     * Attempts to authenticate the provided {@link Authentication} object by iterating through the list of
     * {@link AuthenticationProvider}s.
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : providers) {
            if (!provider.supports(authentication.getClass())) {
                continue;
            }
            Authentication result = provider.authenticate(authentication);
            log.info("Authentication provider [{}] returned: {}", provider.getClass().getSimpleName(), result);
            if (result != null) {
                SecurityContextHolder.getContext().setAuthentication(result);
                return result;
            }
        }
        throw new AuthenticationException("No provider found for " + authentication.getClass().getName()) {};
    }
}
