package pl.edu.zut.app.parking.parking.kafka;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.kafka.messages.SessionStartedMessage;

@Component
@RequiredArgsConstructor
public class SessionStartedMessageProducer {

  private final KafkaTemplate<String, SessionStartedMessage> kafkaTemplate;

  /**
   * Sends a Kafka message regarding the start of a parking session.
   *
   * @param plateNumber the vehicle's license plate number
   * @param parkingId the unique identifier of the parking session
   * @param ownerId the unique identifier of the vehicle owner
   * @param startTime the start time of the parking session
   */
  public void produce(String plateNumber, UUID parkingId, UUID ownerId, LocalDateTime startTime, String parkingName,
                      String address) {
    Message<SessionStartedMessage> message =
        MessageBuilder.withPayload(
                new SessionStartedMessage(null, plateNumber, startTime, ownerId, parkingId, null))
            .setHeader(KafkaHeaders.TOPIC, "session-started-parking")
            .build();
    kafkaTemplate.send(message);
  }
}
