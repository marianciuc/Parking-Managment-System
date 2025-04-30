package pl.edu.zut.app.parking.tariffs.dto;

import pl.edu.zut.app.parking.tariffs.enums.TariffClass;

import java.math.BigDecimal;

public record UpdateTariffRequest(
        String name,
        String description,
        TariffClass tariffClass,
        BigDecimal price
) {
}
