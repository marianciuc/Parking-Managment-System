package pl.edu.zut.app.parking.auth.security.providers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUser;

@Slf4j
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationUserDetailsService<Authentication> userDetailsService;

    public JWTAuthenticationProvider(AuthenticationUserDetailsService<Authentication> userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authenticating user with JWT token provided");

        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        log.info("Token: {}", authToken);

        AuthenticatedUser userPrincipal = (AuthenticatedUser) userDetailsService.loadUserDetails(authentication);
        log.info("User found: {}", userPrincipal);
        if (authToken != null) {
            return new PreAuthenticatedAuthenticationToken(userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities());
        }
        throw new BadCredentialsException("Invalid token");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
