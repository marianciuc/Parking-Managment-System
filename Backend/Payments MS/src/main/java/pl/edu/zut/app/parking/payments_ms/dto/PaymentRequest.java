package pl.edu.zut.app.parking.payments_ms.dto;

import java.math.BigDecimal;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;

public record PaymentRequest(BigDecimal amount, Currency currency) {}
