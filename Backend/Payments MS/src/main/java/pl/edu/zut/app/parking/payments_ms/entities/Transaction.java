package pl.edu.zut.app.parking.payments_ms.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.exceptions.IllegalTransactionStatusException;

/**
 * Represents a financial transaction within the system.
 * <p>A transaction can involve the transfer of funds between accounts or other operations. It
 * tracks the amount, currency, type, and status of the transaction. Each transaction also has
 * optional metadata such as a stripe payment ID, associated bank account, subscription order ID,
 * and a session ID.
 *
 * <p>This class supports linked entities such as accounts (source and destination) and optional
 * relationships like bank accounts or subscriptions.
 */
@Getter
@Setter
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "transactions")
public class Transaction extends AbstractBaseEntity {

  @ManyToOne
  @JoinColumn(name = "source_account_id")
  private AccountBalance sourceAccount;

  @ManyToOne
  @JoinColumn(name = "destination_account_id")
  private AccountBalance destinationAccount;

  @Embedded
  private ConversionDetails conversionDetails;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TransactionStatus status = TransactionStatus.PENDING;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType type;

  @Embedded
  private TransactionMetadata metadata;

  @Column(name = "failure_message")
  private String failureMessage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bank_account_id", updatable = false)
  private BankAccount bankAccount;
}
