package pl.edu.zut.app.parking.parking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.kafka.messages.ChangeParkingCurrencyMessage;
import pl.edu.zut.app.parking.parking.kafka.messages.CreatedParkingMessage;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeParkingCurrencyMessageProducer {

    private final KafkaTemplate<String, ChangeParkingCurrencyMessage> kafkaTemplate;

    public void send(List<UUID> parkingIds, String currency) {
        Message<ChangeParkingCurrencyMessage> message = MessageBuilder
                .withPayload(new ChangeParkingCurrencyMessage(parkingIds, currency))
                .setHeader(KafkaHeaders.TOPIC, "change-parking-currency-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
