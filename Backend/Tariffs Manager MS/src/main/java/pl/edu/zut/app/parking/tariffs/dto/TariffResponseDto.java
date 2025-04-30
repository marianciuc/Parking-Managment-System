package pl.edu.zut.app.parking.tariffs.dto;

import pl.edu.zut.app.parking.tariffs.entities.Tariff;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public record TariffResponseDto(
        UUID id,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        UUID parkingId,
        String name,
        String description,
        String tariffClass,
        HashMap<String, BigDecimal> prices
        ) {
    public static TariffResponseDto from(Tariff tariff, HashMap<String, BigDecimal> prices) {
        return new TariffResponseDto(
                tariff.getId(),
                tariff.getCreationDate(),
                tariff.getModificationDate(),
                tariff.getParkingId(),
                tariff.getName(),
                tariff.getDescription(),
                tariff.getTariffClass().name(),
                prices
        );
    }
}
