package pl.edu.zut.app.parking.payments_ms.specifications;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.payments_ms.entities.Transaction;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;

/**
 * A utility class for building JPA specifications for querying {@code Transaction} entities. This
 * class provides a set of static methods to create specifications based on various attributes of
 * the {@code Transaction} entity, enabling dynamic construction of database queries.
 *
 * <p>Each method returns a JPA {@code Specification} that can be used in a query to filter
 * transactions based on the provided criteria. The specifications can be combined using logical
 * operators to build complex queries.
 *
 * <p>The class includes specifications for attributes such as: - Transaction ID, Bank Account ID,
 * and Parking ID - Transaction Type and Status - Source and Destination Account IDs - Metadata
 * properties like session ID, stripe payment ID, and subscription order ID
 *
 * <p>The {@code build} method provides a way to construct a combined specification based on
 * multiple filters. It is primarily used for creating aggregate queries with multiple criteria.
 *
 * <p>Notes: - Null input values for any criteria result in a null specification, meaning no
 * filtering is applied for that field. - Metadata attributes are accessed using a configured
 * metadata prefix ("metadata").
 */
@Component
public class TransactionSpecifications {

  private static final String METADATA_PREFIX = "metadata";

  /**
   * Creates a specification to filter {@link Transaction} entities by their ID.
   *
   * @param id the UUID of the transaction to filter by; if null, no filter will be applied for the
   *     ID
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified ID, or
   *     null if the ID is null
   */
  public static Specification<Transaction> whereId(UUID id) {
    return (root, query, cb) -> {
      if (id == null) {
        return null;
      }
      return cb.equal(root.get("id"), id);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their associated bank account
   * ID.
   *
   * @param id the UUID of the associated bank account to filter by; if null, no filter will be
   *     applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified bank
   *     account ID, or null if the ID is null
   */
  public static Specification<Transaction> whereBankAccountId(UUID id) {
    return (root, query, cb) -> {
      if (id == null) {
        return null;
      }
      return cb.equal(root.get("bankAccountId"), id);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their transaction type.
   *
   * @param transactionType the {@link TransactionType} to filter by; if null, no filter will be
   *     applied for the transaction type
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified
   *     transaction type, or null if the transaction type is null
   */
  public static Specification<Transaction> whereTransactionTypeIs(TransactionType transactionType) {
    return (root, query, cb) -> {
      if (transactionType == null) {
        return null;
      }
      return cb.equal(root.get("transactionType"), transactionType);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their associated parking ID.
   *
   * @param id the UUID of the associated parking ID to filter by; if null, no filter will be
   *     applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified parking
   *     ID, or null if the ID is null
   */
  public static Specification<Transaction> whereParkingId(UUID id) {
    return (root, query, cb) -> {
      if (id == null) {
        return null;
      }
      return cb.equal(root.get("metadata").get("parkingId"), id);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their transaction status.
   *
   * @param status the {@link TransactionStatus} to filter by; if null, no filter will be applied
   *     for the status
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified
   *     transaction status, or null if the status is null
   */
  public static Specification<Transaction> whereStatusIs(TransactionStatus status) {
    return (root, query, cb) -> {
      if (status == null) {
        return null;
      }
      return cb.equal(root.get("status"), status);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their session ID.
   *
   * @param sessionId the UUID of the session to filter by; if null, no filter will be applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified session
   *     ID, or null if the session ID is null
   */
  public static Specification<Transaction> whereSessionIdIs(UUID sessionId) {
    return (root, query, cb) -> {
      if (sessionId == null) {
        return null;
      }
      return cb.equal(root.get(METADATA_PREFIX).get("sessionId"), sessionId);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their Stripe payment ID.
   *
   * @param stripePaymentId the Stripe payment ID to filter by; if null, no filter will be applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified Stripe
   *     payment ID, or null if the Stripe payment ID is null
   */
  public static Specification<Transaction> whereStripePaymentIdIs(String stripePaymentId) {
    return (root, query, cb) -> {
      if (stripePaymentId == null) {
        return null;
      }
      return cb.equal(root.get(METADATA_PREFIX).get("stripePaymentId"), stripePaymentId);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their subscription order ID.
   *
   * @param subscriptionOrderId the subscription order ID to filter by; if null, no filter will be
   *     applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified
   *     subscription order ID, or null if the subscription order ID is null
   */
  public static Specification<Transaction> whereSubscriptionOrderIdIs(String subscriptionOrderId) {
    return (root, query, cb) -> {
      if (subscriptionOrderId == null) {
        return null;
      }
      return cb.equal(root.get(METADATA_PREFIX).get("subscriptionOrderId"), subscriptionOrderId);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their source account ID.
   *
   * @param sourceAccountId the UUID of the source account to filter by; if null, no filter will be
   *     applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified source
   *     account ID, or null if the source account ID is null
   */
  public static Specification<Transaction> whereSourceAccountIdIs(UUID sourceAccountId) {
    return (root, query, cb) -> {
      if (sourceAccountId == null) {
        return null;
      }
      return cb.equal(root.get("sourceAccount").get("id"), sourceAccountId);
    };
  }

  /**
   * Creates a specification to filter {@link Transaction} entities by their destination account ID.
   *
   * @param destinationAccountId the UUID of the destination account to filter by; if null, no
   *     filter will be applied
   * @return a {@link Specification} to filter {@link Transaction} entities by the specified
   *     destination account ID, or null if the destination account ID is null
   */
  public static Specification<Transaction> whereDestinationAccountIdIs(UUID destinationAccountId) {
    return (root, query, cb) -> {
      if (destinationAccountId == null) {
        return null;
      }
      return cb.equal(root.get("destinationAccount").get("id"), destinationAccountId);
    };
  }

  /**
   * Builds a composite {@link Specification} to filter {@link Transaction} entities based on a
   * combination of filter criteria.
   *
   * @param transactionId the UUID of the transaction to filter by; if null, no filter is applied
   *     for the transaction ID
   * @param bankAccountId the UUID of the associated bank account to filter by; if null, no filter
   *     is applied for the bank account
   * @param status the {@link TransactionStatus} to filter by; if null, no filter is applied for the
   *     status
   * @param sourceAccountId the UUID of the source account to filter by; if null, no filter is
   *     applied for the source account
   * @param destinationAccountId the UUID of the destination account to filter by; if null, no
   *     filter is applied for the destination account
   * @param parkingId the UUID of the associated parking to filter by; if null, no filter is applied
   *     for the parking ID
   * @param sessionId the UUID of the associated session to filter by; if null, no filter is applied
   *     for the session ID
   * @param stripePaymentId the Stripe payment ID to filter by; if null, no filter is applied for
   *     the Stripe payment ID
   * @param transactionType the {@link TransactionType} to filter by; if null, no filter is applied
   *     for the transaction type
   * @param subscriptionOrderId the subscription order ID to filter by; if null, no filter is
   *     applied for the subscription order ID
   * @return a {@link Specification} object that combines the specified filters to match {@link
   *     Transaction} entities; null for any unspecified or null parameter values
   */
  public static Specification<Transaction> build(
      UUID transactionId,
      UUID bankAccountId,
      TransactionStatus status,
      UUID sourceAccountId,
      UUID destinationAccountId,
      UUID parkingId,
      UUID sessionId,
      String stripePaymentId,
      TransactionType transactionType,
      String subscriptionOrderId) {
    return Specification.where(whereStatusIs(status))
        .and(whereTransactionTypeIs(transactionType))
        .and(whereSourceAccountIdIs(sourceAccountId))
        .and(whereBankAccountId(bankAccountId))
        .and(whereId(transactionId))
        .and(whereParkingId(parkingId))
        .and(whereSessionIdIs(sessionId))
        .and(whereStripePaymentIdIs(stripePaymentId))
        .and(whereSubscriptionOrderIdIs(subscriptionOrderId))
        .and(whereDestinationAccountIdIs(destinationAccountId));
  }
}
