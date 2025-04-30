package pl.edu.zut.app.parking.auth.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.zut.app.parking.auth.enums.Possibilities;

import java.util.*;
import java.util.stream.Collectors;

public class AuthenticatedUser implements AuthenticatedUserDetails {

    @Getter
    private final UUID id;
    private final List<GrantedAuthority> authorities;

    public AuthenticatedUser(UUID id, List<String> tokenPossibilities, Set<Possibilities> possibilities) {
        this.id = id;
        this.authorities = convertAuthorities(tokenPossibilities, possibilities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){ 
        return this.authorities;
  }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    private List<GrantedAuthority> convertAuthorities(List<String> authorities, Set<Possibilities> possibilities) {
        if (authorities == null || possibilities == null) {
            return List.of();
        }

        List<GrantedAuthority> grantedAuthorities = possibilities.stream().map(Possibilities::getAuthority).collect(Collectors.toList());

        grantedAuthorities.addAll(authorities.stream()
                .filter((authority) -> authority.startsWith("REFRESH_"))
                .map(SimpleGrantedAuthority::new)
                .toList());

        return grantedAuthorities;
    }
}
