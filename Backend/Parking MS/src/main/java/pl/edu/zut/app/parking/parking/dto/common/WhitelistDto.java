package pl.edu.zut.app.parking.parking.dto.common;

import pl.edu.zut.app.parking.parking.entities.Whitelist;

import java.time.LocalDateTime;
import java.util.UUID;

public record WhitelistDto(
        UUID id,
        UUID parkingId,
        UUID vehicleId,
        String vehiclePlate,
        UUID tariffId,
        String tariffName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static WhitelistDto from(Whitelist whitelist) {
        return new WhitelistDto(
                whitelist.getId(),
                whitelist.getParking().getId(),
                whitelist.getVehicleId(),
                whitelist.getPlateNumber(),
                whitelist.getTariffId(),
                whitelist.getTariffName(),
                whitelist.getCreationDate(),
                whitelist.getModificationDate()
        );
    }
}
