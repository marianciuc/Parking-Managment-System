package pl.edu.zut.app.parking.payments_ms.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

public record ParkingRevenue(
    LocalDate date, BigDecimal amount, Currency currency, String currencySymbol) {}
