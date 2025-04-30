package pl.edu.zut.app.parking.tariffs.dto.external.responces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.edu.zut.app.parking.tariffs.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateResDto(
        Currency sourceCurrency,
        Currency destinationCurrency,
        BigDecimal conversionRate,
        LocalDateTime currencyDate
) {
}
