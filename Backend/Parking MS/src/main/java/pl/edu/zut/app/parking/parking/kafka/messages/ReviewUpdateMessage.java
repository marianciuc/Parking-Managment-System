package pl.edu.zut.app.parking.parking.kafka.messages;

import java.util.UUID;

public record ReviewUpdateMessage(
        UUID parkingId,
        double rating,
        int reviewCount
) {
}
