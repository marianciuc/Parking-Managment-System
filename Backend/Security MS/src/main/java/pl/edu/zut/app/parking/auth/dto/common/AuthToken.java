package pl.edu.zut.app.parking.auth.dto.common;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Represents an immutable authentication token in the system.
 * This token is tied to a specific user and has a set of roles, issuer information,
 * and timestamps (in UTC) for when it was created and when it expires.
 */
public record AuthToken (
        UUID tokenId,
        String subject,
        String issuer,
        List<String> roles,
        Instant createdAt,
        Instant expiresAt
) implements Serializable {
    public AuthToken {
        if (tokenId == null) {
            throw new IllegalArgumentException("Token ID cannot be null");
        }
        if (createdAt == null || expiresAt == null) {
            throw new IllegalArgumentException("Creation and expiration timestamps cannot be null");
        }
        if (expiresAt.isBefore(createdAt)) {
            throw new IllegalArgumentException("Expiration timestamp must be after creation timestamp");
        }
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be null or empty");
        }
        roles = List.copyOf(roles);
    }
}
