package pl.edu.zut.app.parking.notifications.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.notifications.enums.NotificationTopic;
import pl.edu.zut.app.parking.notifications.kafka.messages.*;
import pl.edu.zut.app.parking.notifications.services.EmailService;
import pl.edu.zut.app.parking.notifications.services.NotificationService;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

  private final EmailService emailService;
  private final NotificationService notificationService;

  @KafkaListener(topics = "session-started")
  public void consume(SessionStartedMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.SESSION_STARTED);
    }
  }

  @KafkaListener(topics = "session-ended")
  public void consume(SessionEndedMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.SESSION_ENDED);
    }
  }

  @KafkaListener(topics = "successful-session-payment")
  public void consume(SessionPaymentMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.SUCCESSFUL_SESSION_PAYMENT);
      emailService.sendEmailSessionPayment(message);
    }
  }

  @KafkaListener(topics = "low-balance")
  public void consume(LowBalanceMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.LOW_BALANCE);
      emailService.sendEmailLowBalanceMessage(message);
    }
  }

  @KafkaListener(topics = "successful-deposit")
  public void consume(SuccesfulDepositMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.SUCCESSFUL_DEPOSIT);
      emailService.sendEmailSuccessfulDeposit(message);
    }
  }

  @KafkaListener(topics = "failure-deposit")
  public void consume(FailureDepositMessage message) {
    if (message.userId() != null) {
      notificationService.sendPersonalNotification(
          message.userId(), message, NotificationTopic.FAILURE_DEPOSIT);
      emailService.sendEmailFailureDeposit(message);
    }
  }
}
