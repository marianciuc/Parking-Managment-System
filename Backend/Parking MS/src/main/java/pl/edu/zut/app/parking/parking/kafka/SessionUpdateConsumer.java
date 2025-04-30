package pl.edu.zut.app.parking.parking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.enums.PlaceType;
import pl.edu.zut.app.parking.parking.kafka.messages.SessionEndedMessage;
import pl.edu.zut.app.parking.parking.kafka.messages.SessionStartedMessage;
import pl.edu.zut.app.parking.parking.services.CapacityService;
import pl.edu.zut.app.parking.parking.services.ParkingService;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionUpdateConsumer {

  private final CapacityService capacityService;
  private final ParkingService parkingService;
  private final SessionStartedMessageProducer sessionStartedMessageProducer;

  private static final String ADDRESS_FORMATTED = "%s, %s, %s";
  private final SessionEndedMessageProducer sessionEndedMessageProducer;

  @KafkaListener(topics = "session-started", groupId = "${spring.kafka.consumer.group-id}")
  public void consume(SessionStartedMessage message) {
    try {
      if (message == null) {
        log.error("Received null message, skipping processing.");
        return;
      }
      log.info("Received SessionStartedMessage: {}", message);

      capacityService.carIn(message.parkingId(), 1, PlaceType.REGULAR);
      ParkingDetailsResponse parkingDetailsResponse =
          parkingService.findParkingDetailsById(message.parkingId());
      sessionStartedMessageProducer.produce(
          message.plateNumber(),
          parkingDetailsResponse.id(),
          message.userId(),
          message.timeStarted(),
          parkingDetailsResponse.name(),
          String.format(
              ADDRESS_FORMATTED,
              parkingDetailsResponse.address().city(),
              parkingDetailsResponse.address().street(),
              parkingDetailsResponse.address().buildingNumber()));
    } catch (Exception e) {
      log.error("Error while processing message: {}", message, e);
      throw e;
    }
  }

  @KafkaListener(topics = "session-ended", groupId = "${spring.kafka.consumer.group-id}")
  public void consume(SessionEndedMessage message) {
    try {
      if (message == null) {
        log.error("Received null message, skipping processing.");
        return;
      }
      log.info("Received Session Ended Messafe: {}", message);

      capacityService.carOut(message.parkingId(), 1, PlaceType.REGULAR);

      ParkingDetailsResponse parkingDetailsResponse =
          parkingService.findParkingDetailsById(message.parkingId());
      sessionEndedMessageProducer.produce(
          message.plateNumber(),
          message.parkingId(),
          message.userId(),
          message.timeStarted(),
          message.endTime(),
          parkingDetailsResponse.name(),
          String.format(
              ADDRESS_FORMATTED,
              parkingDetailsResponse.address().city(),
              parkingDetailsResponse.address().street(),
              parkingDetailsResponse.address().buildingNumber()));
    } catch (Exception e) {
      log.error("Error while processing message: {}", message, e);
      throw e;
    }
  }
}
