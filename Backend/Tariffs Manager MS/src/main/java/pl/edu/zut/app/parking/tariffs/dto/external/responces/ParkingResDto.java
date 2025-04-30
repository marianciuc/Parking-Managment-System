package pl.edu.zut.app.parking.tariffs.dto.external.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ParkingResDto(
        UUID id,
        String name,
        String imageUrl,
        String parkingStatus,
        Boolean is24h,
        boolean isPinned,
        double rating,
        String recordStatus,
        UUID ownerId
) {
}
