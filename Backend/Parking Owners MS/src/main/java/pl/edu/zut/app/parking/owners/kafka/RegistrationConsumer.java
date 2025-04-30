package pl.edu.zut.app.parking.owners.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.owners.kafka.messages.CreatedUserMessage;
import pl.edu.zut.app.parking.owners.services.ParkingOwnerRegistrationService;


@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationConsumer {

    private final ParkingOwnerRegistrationService registrationService;

    @KafkaListener(topics = "register-owner", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOwnerRegistrationMessage(CreatedUserMessage message) {
        try {
            if (message == null) {
                log.error("Received null message, skipping processing.");
                return;
            }
            log.info("Received CreatedUserMessage: {}", message);
            registrationService.createParkingOwner(message.getUserId());
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
            throw e;
        }
    }

}
