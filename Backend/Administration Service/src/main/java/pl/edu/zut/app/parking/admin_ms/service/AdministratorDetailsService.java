package pl.edu.zut.app.parking.admin_ms.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorDto;
import pl.edu.zut.app.parking.admin_ms.dto.AdministratorUpdateRequest;
import pl.edu.zut.app.parking.admin_ms.exception.AdministratorAlreadyExistsException;
import pl.edu.zut.app.parking.admin_ms.exception.AdministratorNotFountException;

public interface AdministratorDetailsService {
  /**
   * Creates a new administrator in the system associated with the given user ID.
   *
   * @param userId the unique identifier of the user to be assigned as an administrator
   * @throws AdministratorAlreadyExistsException if an administrator with the specified user ID already exists
   */
  void createAdministrator(UUID userId) throws AdministratorAlreadyExistsException;

  /**
   * Updates the details of an existing administrator identified by the given user ID.
   *
   * @param userId the unique identifier of the administrator to be updated
   * @param administratorUpdateRequest the object containing updated details for the administrator
   * @return the updated AdministratorDto containing the administrator's new details
   * @throws AdministratorNotFountException if no administrator is found with the specified user ID
   */
  AdministratorDto updateAdministrator(
      UUID userId, AdministratorUpdateRequest administratorUpdateRequest) throws AdministratorNotFountException;

  /**
   * Checks if the administrator associated with the specified user ID has the required permissions.
   *
   * @param userId the unique identifier of the user to check permissions for
   * @return true if the administrator has the required permissions, false otherwise
   * @throws AdministratorNotFountException if no administrator is found with the specified user ID
   */
  boolean administratorHasPermission(UUID userId) throws AdministratorNotFountException;

  /**
   * Terminates the administrator associated with the specified user ID.
   * This operation marks the administrator as inactive and sets the termination date.
   *
   * @param userId the unique identifier of the administrator to terminate
   * @return the updated AdministratorDto containing the administrator's details after termination
   * @throws AdministratorNotFountException if no administrator is found with the specified user ID
   */
  AdministratorDto terminateAdministrator(UUID userId) throws AdministratorNotFountException;

  /**
   * Retrieves a paginated list of administrators based on the specified parameters.
   *
   * @param page the page number to retrieve, starting from 0
   * @param size the number of records per page
   * @param sort the field by which the records are sorted
   * @param direction the sorting direction, can be "ASC" or "DESC"
   * @param firstName the first name to filter the administrators
   * @param lastName the last name to filter the administrators
   * @return a {@code Page} containing a list of {@code AdministratorDto} objects, each representing
   *         an administrator that matches the specified filters and pagination
   */
  Page<AdministratorDto> findAllAdministrators(
      int page, int size, String sort, String direction, String firstName, String lastName);

  /**
   * Retrieves the details of an administrator based on the provided user ID.
   *
   * @param userId the unique identifier of the administrator whose details are to be retrieved
   * @return an {@code AdministratorDto} containing the details of the administrator
   * @throws AdministratorNotFountException if no administrator is found with the specified user ID
   */
  AdministratorDto getAdministratorDetails(UUID userId) throws AdministratorNotFountException;
}


