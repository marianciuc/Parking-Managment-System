package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessionEndedMessage(
    String formattedAddress,
    UUID parkingId,
    String parkingName,
    String plateNumber,
    LocalDateTime timeStarted,
    LocalDateTime endTime,
    UUID userId)
    implements Serializable {}
