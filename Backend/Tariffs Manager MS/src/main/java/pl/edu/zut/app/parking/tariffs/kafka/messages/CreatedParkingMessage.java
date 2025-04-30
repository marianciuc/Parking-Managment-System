package pl.edu.zut.app.parking.tariffs.kafka.messages;

import java.util.UUID;

public record CreatedParkingMessage(
        UUID parkingId
) {
}
