package pl.edu.zut.app.parking.parking.kafka.messages;

import java.util.List;
import java.util.UUID;

public record ChangeParkingCurrencyMessage(
        List<UUID> parkingIds,
        String currency
) {
}
