package pl.edu.zut.app.parking.payments_ms.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.exceptions.TransactionCreationException;

/**
 * Service interface for handling operations related to transactions, such as creation, retrieval,
 * updates, and filtering of various types of transactions.
 */
public interface TransactionService {

  /**
   * Retrieves the details of a specific transaction based on the provided transaction ID.
   *
   * @param transactionId the unique identifier of the transaction to retrieve
   * @return a {@link TransactionDto} representing the details of the transaction
   */
  TransactionDto getTransaction(UUID transactionId);

  /**
   * Creates a top-up balance transaction for the specified account with a given amount and
   * currency.
   *
   * @param accountId the unique identifier of the account to be credited
   * @param amount the top-up amount to be added to the account balance
   * @param currency the currency in which the transaction is denominated
   * @return a {@link TransactionDto} representing the created top-up balance transaction
   */
  TransactionDto createTopUpBalanceTransaction(
      UUID accountId, BigDecimal amount, Currency currency);

  /**
   * Updates the status of a specified transaction, marking it as successful or failed, and
   * optionally providing a failure message for failed transactions.
   *
   * @param transactionId the unique identifier of the transaction to update
   * @param isSuccess a boolean indicating whether the transaction was successful
   * @param failureMessage a message describing the reason for transaction failure, or null if the
   *     transaction was successful
   */
  void updateTransactionStatus(UUID transactionId, boolean isSuccess, String failureMessage);

  /**
   * Creates a session payment transaction in the system.
   *
   * @param type the type of the transaction being created, e.g., SESSIONS_PAYMENT.
   * @param sessionId the unique identifier of the parking session associated with the transaction.
   * @param amount the amount of the transaction.
   * @param currency the currency in which the transaction is denominated.
   * @param parkingId the unique identifier of the parking facility where the session took place.
   * @param parkingBuisnessAccountId the unique identifier of the parking business account that
   *     receives the payment.
   * @param carOwenrAccountId the unique identifier of the car owner's account that initiates the
   *     payment.
   * @return a {@link TransactionDto} representing the created session payment transaction.
   * @throws TransactionCreationException if there is an error during the transaction creation
   *     process.
   */
  TransactionDto createSessionPaymentTransaction(
      TransactionType type,
      UUID sessionId,
      BigDecimal amount,
      Currency currency,
      UUID parkingId,
      UUID parkingBuisnessAccountId,
      UUID carOwenrAccountId)
      throws TransactionCreationException;

  /**
   * Finds and retrieves a paginated list of transactions filtered and sorted according to the
   * provided parameters.
   *
   * @param page the page number to retrieve (0-based index)
   * @param size the number of transactions per page
   * @param sort the property by which to sort the transactions
   * @param direction the direction of sorting (e.g., ASC or DESC)
   * @param transactionId the unique identifier of the specific transaction to find
   * @param bankAccountId the unique identifier of the associated bank account
   * @param status the status of the transactions (e.g., PENDING, COMPLETED, FAILED)
   * @param sourceAccountId the unique identifier of the source account in the transaction
   * @param destinationAccountId the unique identifier of the destination account in the transaction
   * @param parkingId the unique identifier of the parking facility related to the transaction
   * @param sessionId the unique identifier of the session associated with the transaction
   * @param stripePaymentId the identifier of the payment in Stripe, if applicable
   * @param converted a flag indicating whether the transaction involves currency conversion
   * @param currency the currency of the transaction
   * @param transactionType the type of the transaction (e.g., PAYMENT, TOP_UP, WITHDRAWAL)
   * @param subscriptionOrderId the unique identifier of a related subscription order, if applicable
   * @return a paginated {@code Page} containing {@code TransactionDto} objects matching the
   *     specified filters
   */
  Page<TransactionDto> findTransactions(
      Integer page,
      Integer size,
      String sort,
      String direction,
      UUID transactionId,
      UUID bankAccountId,
      TransactionStatus status,
      UUID sourceAccountId,
      UUID destinationAccountId,
      UUID parkingId,
      UUID sessionId,
      String stripePaymentId,
      Boolean converted,
      Currency currency,
      TransactionType transactionType,
      String subscriptionOrderId);

  /**
   * Creates a withdraw transaction for a specified account balance and bank account.
   *
   * @param amount the amount to be withdrawn
   * @param currency the currency of the transaction
   * @param accountBalanceId the unique identifier of the account balance involved in the withdrawal
   * @param bankAccountId the unique identifier of the bank account for the withdrawal
   * @return a {@link TransactionDto} representing the created withdrawal transaction
   */
  TransactionDto createWithdrawTransaction(
      BigDecimal amount, Currency currency, UUID accountBalanceId, UUID bankAccountId);
}
