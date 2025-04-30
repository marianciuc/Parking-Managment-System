package pl.edu.zut.app.parking.reviews.kafka.messages;

import java.util.UUID;

public record ReviewUpdateMessage (
        UUID parkingId,
        double rating,
        int reviewCount
) {
}
