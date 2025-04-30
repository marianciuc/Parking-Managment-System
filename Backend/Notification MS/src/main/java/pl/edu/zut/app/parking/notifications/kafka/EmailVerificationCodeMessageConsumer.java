package pl.edu.zut.app.parking.notifications.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.notifications.kafka.messages.EmailVerificationMessage;
import pl.edu.zut.app.parking.notifications.services.EmailService;

@Component
@RequiredArgsConstructor
public class EmailVerificationCodeMessageConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "email-verification-code")
    public void consume(EmailVerificationMessage message) {
        emailService.sendEmailVerification(message.email(), message.verificationCode());
    }
}
