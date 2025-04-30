package pl.zut.edu.app.parking.sessions.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
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
