package pl.edu.zut.app.parking.auth.kafka.messages;

import java.util.UUID;

public record CreatedDriverMessage(
        UUID driverId,
        String email,
        String givenName,
        String familyName,
        String pictureUrl,
        Boolean emailVerified,
        String locale
) {
}
