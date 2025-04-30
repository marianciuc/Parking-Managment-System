package pl.edu.zut.app.parking.reviews.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.reviews.kafka.messages.ReviewUpdateMessage;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewsUpdateMessageProducer {

    private final KafkaTemplate<String, ReviewUpdateMessage> kafkaTemplate;

    public void sendNewReviewMessage(UUID reviewId, double rating) {
        Message<ReviewUpdateMessage> message = MessageBuilder
                .withPayload(new ReviewUpdateMessage(reviewId, rating, 1))
                .setHeader(KafkaHeaders.TOPIC, "update-reviews-topic")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendDeleteReviewMessage(UUID reviewId, double rating) {
        Message<ReviewUpdateMessage> message = MessageBuilder
                .withPayload(new ReviewUpdateMessage(reviewId, rating, -1))
                .setHeader(KafkaHeaders.TOPIC, "update-reviews-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
