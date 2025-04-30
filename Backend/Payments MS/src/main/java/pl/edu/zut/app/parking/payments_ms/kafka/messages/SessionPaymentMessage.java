package pl.edu.zut.app.parking.payments_ms.kafka.messages;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a payment message tied to a specific session.
 *
 * <p>This record is typically used for transferring payment-related information regarding a parking
 * session. It encapsulates the session identifier, payment amount, and currency code.
 *
 * <p>Fields: - sessionId (UUID): The unique identifier of the session for which the payment is
 * made. - amount (BigDecimal): The amount of the payment associated with the session. - code
 * (String): The currency code representing the currency of the payment.
 */
public record SessionPaymentMessage(UUID sessionId, BigDecimal amount, String code) {}
