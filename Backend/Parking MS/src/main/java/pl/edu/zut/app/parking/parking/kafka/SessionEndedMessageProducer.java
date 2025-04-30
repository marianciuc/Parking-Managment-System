package pl.edu.zut.app.parking.parking.kafka;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.kafka.messages.SessionEndedMessage;

@Component
@RequiredArgsConstructor
public class SessionEndedMessageProducer {

  private final KafkaTemplate<String, SessionEndedMessage> kafkaTemplate;

  /**
   * Sends a Kafka message regarding the end of a parking session.
   *
   * @param plateNumber the vehicle's license plate number
   * @param parkingId the unique identifier of the parking session
   * @param ownerId the unique identifier of the vehicle owner
   * @param startTime the start time of the parking session
   * @param endTime the end time of the parking session
   */
  public void produce(
      String plateNumber,
      UUID parkingId,
      UUID ownerId,
      LocalDateTime startTime,
      LocalDateTime endTime, String parkingName, String address) {
    Message<SessionEndedMessage> message =
        MessageBuilder.withPayload(
                new SessionEndedMessage(
                    address, parkingId, parkingName, plateNumber, startTime, endTime, ownerId))
            .setHeader(KafkaHeaders.TOPIC, "session-ended-parking")
            .build();
    kafkaTemplate.send(message);
  }
}
