package pl.edu.zut.app.parking.tariffs.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import pl.edu.zut.app.parking.tariffs.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.tariffs.entities.Tariff;
import pl.edu.zut.app.parking.tariffs.enums.TariffClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import pl.edu.zut.app.parking.tariffs.enums.Currency;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
public record TariffDto(
        UUID id,
        LocalDateTime creationDate,
        LocalDateTime modificationDate,
        AbstractBaseEntity.RecordStatus recordStatus,
        Integer version,

        UUID parkingId,

        @NotNull(message = "Name cannot be null")
        @NotEmpty(message = "Name cannot be empty")
        String name,

        @NotNull(message = "Description cannot be null")
        @NotEmpty(message = "Description cannot be empty")
        String description,

        @NotNull(message = "Tariff class cannot be null")
        TariffClass tariffClass,

        BigDecimal price,
        pl.edu.zut.app.parking.tariffs.enums.Currency currency
) {
    public static TariffDto fromEntity(Tariff tariff) {
        return new TariffDto(
                tariff.getId(),
                tariff.getCreationDate(),
                tariff.getModificationDate(),
                tariff.getRecordStatus(),
                tariff.getVersion(),
                tariff.getParkingId(),
                tariff.getName(),
                tariff.getDescription(),
                tariff.getTariffClass(),
                tariff.getPrice(),
                tariff.getCurrency()
        );
    }
}
