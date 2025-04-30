package pl.edu.zut.app.parking.parking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.kafka.messages.CreatedParkingMessage;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParkingCreatedMessageProducer {

    private final KafkaTemplate<String, CreatedParkingMessage> kafkaTemplate;

    public void sendParkingCreatedMessage(UUID parkingId) {
        Message<CreatedParkingMessage> message = MessageBuilder
                .withPayload(new CreatedParkingMessage(parkingId))
                .setHeader(KafkaHeaders.TOPIC, "created-parking-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
