package pl.edu.zut.app.parking.parking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.kafka.messages.ReviewUpdateMessage;
import pl.edu.zut.app.parking.parking.services.ParkingService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewsUpdateMessageConsumer {

    private final ParkingService parkingService;

    @KafkaListener(topics = "update-reviews-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOwnerRegistrationMessage(ReviewUpdateMessage message) {
        try {
            if (message == null) {
                log.error("Received null message, skipping processing.");
                return;
            }
            log.info("Received ReviewUpdateMessage: {}", message);
            parkingService.updateRating(message.parkingId(), message.rating(), message.reviewCount());
        } catch (Exception e) {
            log.error("Error while processing message: {}", message, e);
            throw e;
        }
    }
}
