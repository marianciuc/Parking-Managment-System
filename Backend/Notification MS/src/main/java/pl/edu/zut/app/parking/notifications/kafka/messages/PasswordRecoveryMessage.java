package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.util.UUID;

public record PasswordRecoveryMessage(
        UUID userId,
        String email,
        String key
) {
}
