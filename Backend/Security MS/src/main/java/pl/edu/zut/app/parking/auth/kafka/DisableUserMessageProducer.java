package pl.edu.zut.app.parking.auth.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.auth.kafka.messages.CreatedUserMessage;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DisableUserMessageProducer {

    private final KafkaTemplate<String, CreatedUserMessage> kafkaTemplate;


    public void sendParkingOwnerRegistrationMessage(UUID ownerId, String email) {
        Message<CreatedUserMessage> message = MessageBuilder
                .withPayload(new CreatedUserMessage(ownerId, email))
                .setHeader(KafkaHeaders.TOPIC, "register-owner")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendDriverRegistrationMessage(UUID driverId, String email) {
        Message<CreatedUserMessage> message = MessageBuilder
                .withPayload(new CreatedUserMessage(driverId, email))
                .setHeader(KafkaHeaders.TOPIC, "register-driver")
                .build();
        kafkaTemplate.send(message);
    }
}
