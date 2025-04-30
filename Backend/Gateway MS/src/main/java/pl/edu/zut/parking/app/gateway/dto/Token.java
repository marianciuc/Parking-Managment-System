package pl.edu.zut.parking.app.gateway.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Record representing a Token in the authentication system.
 *
 * @param tokenId   the unique identifier of the token
 * @param subject   the user id
 * @param issuer    the issuer of the token
 * @param roles     the roles associated with the token
 * @param createdAt the timestamp when the token was created
 * @param expiresAt the timestamp when the token expires
 */
public record Token(
        UUID tokenId,
        String subject,
        String issuer,
        List<String> roles,
        Instant createdAt,
        Instant expiresAt
) {

    public String getRolesAsStringList() {
        return String.join(",", roles);
    }
}
