package pl.edu.zut.app.parking.payments_ms.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.payments_ms.entities.Transaction;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;

/**
 * Repository interface for managing {@link Transaction} entities.
 *
 * <p>Extends {@link JpaRepository} to provide CRUD operations and {@link JpaSpecificationExecutor}
 * to support query-by-example and other criteria queries.
 */
public interface TransactionsRepository
    extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

  /**
   * Finds all transactions based on the provided type, status, and source account ID.
   *
   * @param type the type of the transactions to filter, as defined by {@link TransactionType}.
   * @param status the status of the transactions to filter, as defined by {@link
   *     TransactionStatus}.
   * @param accountBalanceId the unique identifier of the source account to filter transactions by.
   * @return a list of transactions that match the provided type, status, and source account ID.
   */
  @Query(
      "select t from Transaction t where t.type = ?1 and t.status = ?2 and t.sourceAccount.id = ?3")
  List<Transaction> findAllByTypeAndStatusAndSourceAccountId(
      TransactionType type, TransactionStatus status, UUID accountBalanceId);

  /**
   * Finds all transactions based on the provided type, status, and destination account ID.
   *
   * @param transactionType the type of the transactions to filter, as defined by {@link
   *     TransactionType}.
   * @param transactionStatus the status of the transactions to filter, as defined by {@link
   *     TransactionStatus}.
   * @param accountBalanceId the unique identifier of the destination account to filter transactions
   *     by.
   * @return a list of transactions that match the provided type, status, and destination account
   *     ID.
   */
  @Query(
      "select t from Transaction t where t.type = ?1 and t.status = ?2 and t.destinationAccount.id = ?3")
  List<Transaction> findAllByTypeAndStatusAndDestinationAccountId(
      TransactionType transactionType, TransactionStatus transactionStatus, UUID accountBalanceId);
}
