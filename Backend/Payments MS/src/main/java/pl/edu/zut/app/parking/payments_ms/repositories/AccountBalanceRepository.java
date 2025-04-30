package pl.edu.zut.app.parking.payments_ms.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;

/**
 * Repository interface for managing AccountBalance entities.
 * Extends JpaRepository to provide standard CRUD operations.
 * Provides custom queries for additional functionalities.
 */
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, UUID> {

  /**
   * Checks if an AccountBalance exists with the given owner ID.
   *
   * @param ownerId the unique identifier of the owner to check for
   * @return true if an AccountBalance exists for the given owner ID, false otherwise
   */
  @Query("select (count(a) > 0) from AccountBalance a where a.ownerId = ?1")
  boolean existsByOwenerId(UUID ownerId);

  /**
   * Retrieves the account balance associated with the given owner ID.
   *
   * @param accountId the unique identifier of the owner whose account balance is to be retrieved
   * @return an Optional containing the AccountBalance if found, or an empty Optional if not found
   */
  @Query("select a from AccountBalance a where a.ownerId = ?1")
  Optional<AccountBalance> findAccountBalancesByOwenerId(UUID accountId);

  /**
   * Retrieves the account balance associated with the given account ID.
   *
   * @param id the unique identifier of the account whose balance is to be retrieved
   * @return an Optional containing the AccountBalance if found, or an empty Optional if not found
   */
  @Query("select a from AccountBalance a where a.id = ?1")
  Optional<AccountBalance> findAccountBalanceById(UUID id);

  /**
   * Retrieves a paginated list of AccountBalance entities that match the provided specification.
   *
   * @param specification the specification used to filter the AccountBalance entities
   * @param pageable the pageable object defining pagination and sorting
   * @return a Page containing the AccountBalance entities that match the specification
   */
  @Query("select a from AccountBalance a")
  Page<AccountBalance> findAll(Specification<AccountBalance> specification, Pageable pageable);
}
