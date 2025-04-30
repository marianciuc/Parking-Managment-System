package pl.edu.zut.app.parking.payments_ms.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawRequest(
        UUID bankAccountId,
        BigDecimal amount
) {
}
