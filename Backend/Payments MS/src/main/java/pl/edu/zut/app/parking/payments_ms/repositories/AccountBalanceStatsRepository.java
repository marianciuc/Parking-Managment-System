package pl.edu.zut.app.parking.payments_ms.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalanceStats;
import pl.edu.zut.app.parking.payments_ms.entities.BankAccount;

/**
 * Repository interface for managing AccountBalanceStats entities.
 * Provides methods to perform CRUD operations as well as custom queries
 * to retrieve specific AccountBalanceStats data.
 */
public interface AccountBalanceStatsRepository extends JpaRepository<AccountBalanceStats, UUID> {

    /**
     * Retrieves an AccountBalanceStats entity based on the ID of the associated AccountBalance.
     *
     * @param accountBalanceId the unique identifier of the AccountBalance
     * @return an Optional containing the matching AccountBalanceStats entity if found, or an empty Optional if not
     */
    @Query("select a from AccountBalanceStats a where a.accountBalance.id = ?1")
    Optional<AccountBalanceStats> findByAccountBalance_Id(UUID accountBalanceId);

    /**
     * Retrieves an AccountBalanceStats entity based on the Owner ID of the associated AccountBalance.
     *
     * @param ownerId the unique identifier of the owner associated with an AccountBalance
     * @return an Optional containing the matching AccountBalanceStats entity if found, or an empty Optional if not
     */
    @Query("select a from AccountBalanceStats a where a.accountBalance.ownerId = ?1")
    Optional<AccountBalanceStats> findOneByAccountBalance_OwenerId(UUID ownerId);
}
