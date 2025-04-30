package pl.edu.zut.app.parking.auth.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.auth.kafka.messages.CreatedDriverMessage;
import pl.edu.zut.app.parking.auth.kafka.messages.CreatedUserMessage;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationUserProducer {

    private final KafkaTemplate<String, CreatedUserMessage> kafkaTemplate;

    /**
     * Sends a Kafka message to register a new parking owner.
     *
     * @param ownerId the unique identifier of the parking owner to be registered
     */
    public void sendParkingOwnerRegistrationMessage(UUID ownerId, String email) {
        Message<CreatedUserMessage> message = MessageBuilder
                .withPayload(new CreatedUserMessage(ownerId, email))
                .setHeader(KafkaHeaders.TOPIC, "register-owner")
                .build();
        kafkaTemplate.send(message);
    }


    /**
     * Sends a Kafka message to register a new parking owner.
     *
     * @param ownerId the unique identifier of the parking owner to be registered
     */
    public void sendAdministratorRegistrationMessage(UUID ownerId, String email) {
        Message<CreatedUserMessage> message = MessageBuilder
                .withPayload(new CreatedUserMessage(ownerId, email))
                .setHeader(KafkaHeaders.TOPIC, "register-admin")
                .build();
        kafkaTemplate.send(message);
    }

    /**
     * Sends a Kafka message to register a new driver.
     *
     * @param driverId      the unique identifier of the driver to be registered
     * @param givenName     the driver firstname. It can be null
     * @param familyName    the driver lastname. This field can be null
     * @param pictureUrl    the driver picture url, it can be null too
     * @param emailVerified the driver email verification status, it can be null
     * @param locale        the driver preferred locale, it can be null
     * @throws IllegalArgumentException if the driver id is null
     */
    public void sendDriverRegistrationMessage(UUID driverId, String email, String givenName, String familyName,
                                              String pictureUrl, Boolean emailVerified, String locale) {
        if (driverId == null) throw new IllegalArgumentException("Driver ID cannot be null");

        Message<CreatedDriverMessage> message = MessageBuilder
                .withPayload(new CreatedDriverMessage(
                        driverId,
                        email,
                        givenName,
                        familyName,
                        pictureUrl,
                        emailVerified,
                        locale
                ))
                .setHeader(KafkaHeaders.TOPIC, "register-driver")
                .build();
        kafkaTemplate.send(message);
    }
}
