package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.util.UUID;

public record EmailVerificationMessage(
        UUID userId,
        String email,
        String verificationCode
) {
}
