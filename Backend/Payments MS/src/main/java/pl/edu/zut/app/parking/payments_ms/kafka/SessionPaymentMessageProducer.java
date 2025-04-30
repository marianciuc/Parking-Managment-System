package pl.edu.zut.app.parking.payments_ms.kafka;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.kafka.messages.SessionPaymentMessage;

/**
 * A Kafka producer component responsible for sending payment-related messages.
 *
 * <p>This class is used to publish messages about payments tied to specific sessions. It utilizes
 * the {@link KafkaTemplate} to construct and send messages containing payment details to the
 * appropriate Kafka topic.
 *
 * <p>The messages contain the session identifier and the payment amount. These details are
 * encapsulated in a {@link SessionPaymentMessage}, which serves as the payload for the message.
 *
 * <p>The Kafka topic to which the messages are sent is named "payment-received". This class
 * facilitates reliable message production for use in asynchronous message-driven systems.
 */
@Component
@RequiredArgsConstructor
public class SessionPaymentMessageProducer {

  private final KafkaTemplate<String, SessionPaymentMessage> kafkaTemplate;

  /**
   * Sends a payment message to the Kafka topic "payment-received".
   *
   * <p>This method constructs a {@link SessionPaymentMessage} containing the session ID and payment
   * amount, wraps it in a Kafka message, and publishes it to the specified Kafka topic.
   *
   * @param sessionId the unique identifier of the session for which the payment is made
   * @param amount    the amount of the payment being processed
   * @param currency
   */
  public void sendMessage(UUID sessionId, BigDecimal amount, Currency currency) {
    Message<SessionPaymentMessage> message =
        MessageBuilder.withPayload(new SessionPaymentMessage(sessionId, amount, currency.getCode()))
            .setHeader(KafkaHeaders.TOPIC, "payment-received")
            .build();
    kafkaTemplate.send(message);
  }
}
