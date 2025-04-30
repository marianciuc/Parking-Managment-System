package pl.zut.edu.app.parking.sessions.kafka;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.zut.edu.app.parking.sessions.kafka.messages.SessionPaymentMessage;
import pl.zut.edu.app.parking.sessions.services.SessionService;

@Component
@RequiredArgsConstructor
public class PaymentReceivedMessageConsumer {

    private final SessionService sessionService;

    @KafkaListener(topics = "payment-received", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(SessionPaymentMessage message) {
        sessionService.addPayment(message);
    }
}
