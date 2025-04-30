package pl.edu.zut.app.parking.payments_ms.dto.external;

import pl.edu.zut.app.parking.payments_ms.enums.Currency;

import java.math.BigDecimal;

public record SessionPaymentOrderDto(
        BigDecimal amount,
        Currency currency
) {
}
