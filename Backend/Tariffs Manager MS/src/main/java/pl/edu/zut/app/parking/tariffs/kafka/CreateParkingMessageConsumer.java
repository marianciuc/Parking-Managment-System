package pl.edu.zut.app.parking.tariffs.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.tariffs.kafka.messages.CreatedParkingMessage;
import pl.edu.zut.app.parking.tariffs.services.TariffService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateParkingMessageConsumer {

    private final TariffService tariffService;

    @KafkaListener(topics = "created-parking-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOwnerRegistrationMessage(CreatedParkingMessage message) {
        try {
            if (message == null) {
                log.error("Received null message, skipping processing.");
                return;
            }
            log.info("Received ReviewUpdateMessage: {}", message);
            tariffService.createBaseTariff(message);
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
            throw e;
        }
    }
}
