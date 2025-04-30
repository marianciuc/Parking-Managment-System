package pl.edu.zut.app.parking.payments_ms.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.payments_ms.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.payments_ms.entities.BankAccount;

/**
 * Interface for accessing and managing bank account data in the database. Extends the {@code
 * JpaRepository} to provide CRUD operations and custom query methods for {@code BankAccount}
 * entities.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

  /**
   * Finds a bank account by its ID and ensures its record status does not match the specified
   * status.
   *
   * @param id the unique identifier of the bank account to be retrieved
   * @param recordStatus the record status to exclude during the search
   * @return an {@code Optional} containing the bank account if found and its record status does not
   *     match the specified status, otherwise an empty {@code Optional}
   */
  @Query("select b from BankAccount b where b.id = ?1 and b.recordStatus <> ?2")
  Optional<BankAccount> findByIdAndRecordStatusIsNot(
      UUID id, AbstractBaseEntity.RecordStatus recordStatus);

  /**
   * Retrieves a list of bank accounts by the owner ID of their associated account balance, ensuring
   * their record status does not match the specified status.
   *
   * @param ownerId the unique identifier of the owner of the account balance associated with the
   *     bank accounts
   * @param recordStatus the record status to exclude during the search
   * @return a list of {@code BankAccount} entities matching the criteria
   */
  @Query(
      "select b from BankAccount b where b.accountBalance.ownerId = ?1 and b.recordStatus <> ?2")
  List<BankAccount> findAllByAccountBalance_OwenerIdAndRecordStatusIsNot(
      UUID ownerId, AbstractBaseEntity.RecordStatus recordStatus);
}
