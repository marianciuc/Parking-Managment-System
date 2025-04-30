package pl.edu.zut.parking.app.gateway.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ApiKeyDto(
        UUID id,
        String key,
        UUID parkingId,
        Set<String> scopes,
        String status,
        UUID issuedBy,
        UUID revokedBy,
        LocalDateTime creationDate,
        LocalDateTime modificationDate
) {
}
