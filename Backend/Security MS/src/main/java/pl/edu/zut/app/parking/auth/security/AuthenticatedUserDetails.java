package pl.edu.zut.app.parking.auth.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AuthenticatedUserDetails extends UserDetails {
    UUID getId();
}
