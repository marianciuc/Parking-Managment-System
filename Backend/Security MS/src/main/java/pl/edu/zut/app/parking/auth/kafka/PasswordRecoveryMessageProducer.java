package pl.edu.zut.app.parking.auth.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.auth.kafka.messages.PasswordRecoveryMessage;

import java.util.UUID;

/**
 * A producer class responsible for sending password recovery-related messages to Kafka topics.
 * This class uses a KafkaTemplate to construct and send messages containing password recovery details.
 */
@Component
@RequiredArgsConstructor
public class PasswordRecoveryMessageProducer {
    private final KafkaTemplate<String, PasswordRecoveryMessage> kafkaTemplate;


    /**
     * Sends a Kafka message for password recovery purposes.
     *
     * @param ownerId               the unique identifier of the user requesting password recovery
     * @param email                 the email address of the user
     * @param code                  the password recovery code
     * @param codeExpirationMinutes
     */
    public void sendPasswordRecoveryMessage(UUID ownerId, String email, String code, int codeExpirationMinutes) {
        Message<PasswordRecoveryMessage> message = MessageBuilder
                .withPayload(new PasswordRecoveryMessage(ownerId, email, code))
                .setHeader(KafkaHeaders.TOPIC, "password-recovery-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
