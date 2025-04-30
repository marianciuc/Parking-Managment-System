package pl.edu.zut.app.parking.auth.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.dto.common.AuthToken;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUser;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<Authentication> {

    private final UserRepositoryServiceImpl repository;

    @Override
    public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {

        if (authentication.getPrincipal() instanceof AuthToken authToken) {
            User user = repository.findById(UUID.fromString(authToken.subject()));
            return new AuthenticatedUser(user.getId(), authToken.roles(), user.getUserPossibilities());
        }
        return null;
    }
}
