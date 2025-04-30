package pl.edu.zut.app.parking.notifications.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.notifications.kafka.messages.EmailVerificationMessage;
import pl.edu.zut.app.parking.notifications.kafka.messages.PasswordRecoveryMessage;
import pl.edu.zut.app.parking.notifications.services.EmailService;

@Component
@RequiredArgsConstructor
public class PasswordRecoveryMessageConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "password-recovery-topic")
    public void consume(PasswordRecoveryMessage message) {
        emailService.sendPasswordRecoveryCode(message.email(), message.key());
    }
}
