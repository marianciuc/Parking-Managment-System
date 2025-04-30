package pl.edu.zut.app.parking.auth.dto;

import pl.edu.zut.app.parking.auth.entities.ApiKey;
import pl.edu.zut.app.parking.auth.enums.Scope;

import java.util.Set;
import java.util.UUID;

public record ApiKeyListItem(
    UUID id,
    Set<Scope> scope,
    ApiKey.ApiKeyStatus status,
    UUID issuedBy,
    UUID revokedBy,
    UUID parkingId
) {
    public static ApiKeyListItem fromEntity(ApiKey apiKey) {
        return new ApiKeyListItem(
            apiKey.getId(),
            apiKey.getScope(),
            apiKey.getStatus(),
            apiKey.getIssuedBy(),
            apiKey.getRevokedBy(),
            apiKey.getParkingId()
        );
    }
}
