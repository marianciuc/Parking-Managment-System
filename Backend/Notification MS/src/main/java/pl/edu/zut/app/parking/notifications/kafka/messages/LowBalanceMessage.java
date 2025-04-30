package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record LowBalanceMessage(UUID userId, BigDecimal balance, String email, String currency)
    implements Serializable {}
