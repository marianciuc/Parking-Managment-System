package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionDetails {

    @Column(name = "source_amount", nullable = false, updatable = false)
    private BigDecimal sourceAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_currency", nullable = false, updatable = false)
    private Currency sourceCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "destination_currency", nullable = false, updatable = false)
    private Currency destinationCurrency;

    @Column(name = "destination_amount", nullable = false, updatable = false)
    private BigDecimal destinationAmount;

    @Column(name = "converstation_rate", nullable = false, updatable = false)
    private BigDecimal conversionRate;

    @Column(name = "currency_date", nullable = false, updatable = false)
    private LocalDateTime currencyDate;
}
