package pl.zut.edu.app.parking.sessions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwnershipDto(
        UUID id,
        String recordStatus,
        UUID vehicleId,
        UUID ownerId,
        String ownerType
) {
}
