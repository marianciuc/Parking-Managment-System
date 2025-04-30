package pl.edu.zut.app.parking.payments_ms.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.payments_ms.kafka.messages.ChangeCurrencyMessage;

import java.util.UUID;

/**
 * A Kafka message producer responsible for publishing messages related to changing a user's
 * currency preference.
 *
 * <p>This class utilizes a KafkaTemplate to send {@link ChangeCurrencyMessage} messages to the
 * specified Kafka topic. The messages include the user's unique identifier and the new currency to
 * be set.
 *
 * <p>This component is designed to integrate with a Kafka messaging system, enabling asynchronous
 * processing of currency change requests.
 */
@Component
@RequiredArgsConstructor
public class ChangeCurrencyMessageProducer {
  private final KafkaTemplate<String, ChangeCurrencyMessage> kafkaTemplate;

  /**
   * Sends a message to change the user's currency preference.
   *
   * <p>This method constructs a {@link ChangeCurrencyMessage} containing the user's ID and the new
   * currency, then sends it to the Kafka topic "change-currency-message".
   *
   * @param userId the unique identifier of the user whose currency is being changed
   * @param currency the new currency to set for the user
   */
  public void sendChangeCurrencyMessage(UUID userId, String currency) {
    Message<ChangeCurrencyMessage> message =
        MessageBuilder.withPayload(new ChangeCurrencyMessage(userId, currency))
            .setHeader(KafkaHeaders.TOPIC, "change-currency-topic")
            .build();
    kafkaTemplate.send(message);
  }
}
