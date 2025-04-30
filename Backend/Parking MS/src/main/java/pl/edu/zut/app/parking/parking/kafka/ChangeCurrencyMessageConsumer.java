package pl.edu.zut.app.parking.parking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.kafka.messages.ChangeCurrencyMessage;
import pl.edu.zut.app.parking.parking.services.ParkingService;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeCurrencyMessageConsumer {

    private final ParkingService parkingService;
    private final ChangeParkingCurrencyMessageProducer changeParkingCurrencyMessageProducer;

    @KafkaListener(topics = "change-currency-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeChangeCurrencyMessage(ChangeCurrencyMessage message) {
        try {
            if (message == null) {
                log.error("Received null message, skipping processing.");
                return;
            }
            log.info("Received ReviewUpdateMessage: {}", message);
            List<UUID> parkingDetailsByOwnerId =
                    parkingService.findParkingDetailsByOwnerId(message.userId())
                            .stream().map(ParkingDetailsResponse::id).toList();

            changeParkingCurrencyMessageProducer.send(parkingDetailsByOwnerId, message.currency());
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
            throw e;
        }
    }
}
