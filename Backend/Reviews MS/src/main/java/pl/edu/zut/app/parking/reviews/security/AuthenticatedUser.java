package pl.edu.zut.app.parking.reviews.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthenticatedUser implements UserDetails {

    @Getter
    private final UUID id;
    private final List<GrantedAuthority> authorities;

    public AuthenticatedUser(String id, String authorities) {
        this.id = UUID.fromString(id);
        this.authorities = convertAuthorities(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    private List<GrantedAuthority> convertAuthorities(String authorities) {
        if (authorities == null || authorities.trim().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
