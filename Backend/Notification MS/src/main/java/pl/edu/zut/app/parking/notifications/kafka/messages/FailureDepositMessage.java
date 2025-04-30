package pl.edu.zut.app.parking.notifications.kafka.messages;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message indicating the successful deposit operation for a user. This message
 * contains details such as the user's unique identifier, full name, deposited amount, timestamp of
 * the deposit, email address for contact, and the currency used in the transaction.
 *
 * @param userId the unique identifier of the user who made the deposit
 * @param fullname the full name of the user
 * @param amount the amount of money deposited
 * @param timeOfDeposit the timestamp of when the deposit was made
 * @param email the email address associated with the user
 * @param currency the currency in which the deposit was made
 */
public record FailureDepositMessage(
    UUID userId,
    String fullname,
    BigDecimal amount,
    LocalDateTime timeOfDeposit,
    String reason,
    String email,
    String currency)
    implements Serializable {}
