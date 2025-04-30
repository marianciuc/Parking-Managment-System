package pl.edu.zut.app.parking.payments_ms.kafka.messages;

import java.util.UUID;

public record ChangeCurrencyMessage(
        UUID userId,
        String currency
) {
}
