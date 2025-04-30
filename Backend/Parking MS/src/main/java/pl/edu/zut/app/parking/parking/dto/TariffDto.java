package pl.edu.zut.app.parking.parking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TariffDto(
        UUID id,
        UUID parkingId,
        String name,
        String description,
        String tariffClass,
        BigDecimal price,
        String currency
) {
}
