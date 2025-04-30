package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record SessionPaymentMessage(
    UUID userId,
    UUID sessionId,
    String plateNumber,
    String formattedAddress,
    BigDecimal amount,
    LocalDateTime timeOfDeposit,
    String email,
    String currency)
    implements Serializable {}
