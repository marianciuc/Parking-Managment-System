package pl.edu.zut.app.parking.payments_ms.kafka.messages;

import java.util.UUID;

public record EmailVerificationMessage(
        UUID userId,
        String email,
        String verificationCode
) {
}
