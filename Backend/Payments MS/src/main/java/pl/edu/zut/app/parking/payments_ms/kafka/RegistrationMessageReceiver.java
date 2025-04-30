package pl.edu.zut.app.parking.payments_ms.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.kafka.messages.CreatedDriverMessage;
import pl.edu.zut.app.parking.payments_ms.kafka.messages.CreatedUserMessage;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;

/**
 * A message receiver component for processing Kafka messages related to driver and owner
 * registrations.
 *
 * <p>This class listens to specific Kafka topics and creates account balances for newly registered
 * users, either personal or business, based on the type of registration.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationMessageReceiver {

  private final AccountBalanceService accountBalanceService;

  /**
   * Consumes messages from the "register-driver" Kafka topic and processes them by creating a user
   * account balance.
   *
   * <p>This method listens for messages of type {@link CreatedUserMessage} containing the user's
   * unique identifier. Upon receiving a message, it creates a personal account balance for the user
   * using the {@link AccountBalanceService}. If an error occurs during processing, the exception is
   * logged, and the error is propagated.
   *
   * @param message the message containing the user ID and email of the newly registered driver.
   */
  @KafkaListener(topics = "register-driver", groupId = "${spring.kafka.consumer.group-id}")
  public void consumeOwnerRegistrationMessage(CreatedDriverMessage message) {
    try {
      accountBalanceService.createAccountBalance(message.driverId(), AccountType.PERSONAL);
    } catch (Exception e) {
      log.error("Error while processing message: {}", message, e);
      throw e;
    }
  }

  /**
   * Consumes messages from the "register-owner" Kafka topic and processes them by creating a
   * business account balance.
   *
   * <p>This method listens for messages of type {@link CreatedUserMessage} containing the user's
   * unique identifier. Upon receiving a message, it creates a business account balance for the user
   * using the {@link AccountBalanceService}. If an error occurs during processing, the exception is
   * logged, and the error is propagated.
   *
   * @param message the message containing the user ID and email of the newly registered owner.
   */
  @KafkaListener(topics = "register-owner", groupId = "${spring.kafka.consumer.group-id}")
  public void consumeOwnerRegisterMessage(CreatedUserMessage message) {
    try {
      accountBalanceService.createAccountBalance(message.getUserId(), AccountType.BUSINESS);
    } catch (Exception e) {
      log.error("Error while processing message: {}", message, e);
      throw e;
    }
  }
}
