package pl.edu.zut.app.parking.auth.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.security.AuthenticatedUserDetails;

import java.util.*;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@AllArgsConstructor
@Table(name = "users")
@NoArgsConstructor
public class User extends AbstractBaseEntity implements AuthenticatedUserDetails {

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Builder.Default
    @ElementCollection(targetClass = Possibilities.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_possibilities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "possibility")
    @Enumerated(EnumType.STRING)
    private Set<Possibilities> userPossibilities = EnumSet.noneOf(Possibilities.class);

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_type")
    private UserType userType;

    @Column(name = "is_supported_external_authentication_provider")
    private Boolean isSupportedExternalAuthenticationProvider;

    @Column(name = "is_supporting_login_by_credentials")
    private Boolean isSupportingLoginByCredentials;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<ExternalAuthenticationProvider> externalAuthenticationProviders = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Possibilities possibility : userPossibilities) {
            authorities.add(new SimpleGrantedAuthority(possibility.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.getRecordStatus() != RecordStatus.DELETED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getRecordStatus() != RecordStatus.BANNED;
    }

    @Override
    public boolean isEnabled() {
        return super.getRecordStatus() == RecordStatus.ACTIVE;
    }
}
