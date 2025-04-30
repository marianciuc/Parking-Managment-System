package pl.edu.zut.app.parking.admin_ms.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import pl.edu.zut.app.parking.admin_ms.entity.Administrator;

public interface AdministratorRepository
    extends JpaRepository<Administrator, UUID>, JpaSpecificationExecutor<Administrator> {


  /**
   * Checks whether an Administrator exists with the specified system user ID.
   *
   * @param userId the UUID of the system user to check for existence
   * @return true if an Administrator with the given system user ID exists, false otherwise
   */
  @Query("select (count(a) > 0) from Administrator a where a.systemUserId = ?1")
  boolean existsBySystemUserId(UUID userId);

  /**
   * Retrieves an Administrator entity based on the given system user ID.
   *
   * @param systemUserId the UUID of the system user associated with the Administrator
   * @return an Optional containing the Administrator entity if found, otherwise an empty Optional
   */
  @Query("select a from Administrator a where a.systemUserId = ?1")
  Optional<Administrator> findBySystemUserId(UUID systemUserId);
}
