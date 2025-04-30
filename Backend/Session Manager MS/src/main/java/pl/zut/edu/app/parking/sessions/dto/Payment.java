package pl.zut.edu.app.parking.sessions.dto;

import java.math.BigDecimal;

public record Payment(
        BigDecimal amount,
        String currency,
        long remainingTime
) {
}
