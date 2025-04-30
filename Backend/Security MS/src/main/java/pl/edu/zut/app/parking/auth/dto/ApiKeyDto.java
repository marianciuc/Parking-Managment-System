package pl.edu.zut.app.parking.auth.dto;

import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public static ApiKeyDto fromEntity(ApiKey apiKey) {
        return new ApiKeyDto(
                apiKey.getId(),
                apiKey.getKeyValue(),
                apiKey.getParkingId(),
                apiKey.getScope().stream().map(Scope::name).collect(Collectors.toSet()),
                apiKey.getStatus().name(),
                apiKey.getIssuedBy(),
                apiKey.getRevokedBy(),
                apiKey.getCreationDate(),
                apiKey.getModificationDate()
        );
    }
}
