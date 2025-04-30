package pl.edu.zut.app.parking.auth.kafka.messages;

import java.util.UUID;

public record PasswordRecoveryMessage(
        UUID userId,
        String email,
        String key
) {
}
