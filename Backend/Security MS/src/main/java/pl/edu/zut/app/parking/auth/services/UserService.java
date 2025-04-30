package pl.edu.zut.app.parking.auth.services;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import pl.edu.zut.app.parking.auth.dto.ChangePasswordRequest;
import pl.edu.zut.app.parking.auth.dto.UserDto;
import pl.edu.zut.app.parking.auth.dto.UserSearchCriteria;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;

import java.util.UUID;

/**
 * UserService is responsible for managing user-related operations, such as banning/unbanning users, and retrieving user information.
 */
@Validated
public interface UserService {

    /**
     * Bans the user identified by the given ID.
     *
     * @param id the unique identifier of the user
     * @return the updated {@link UserDto} for the banned user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    UserDto ban(@NotNull UUID id);

    /**
     * Removes a ban for the user identified by the given ID.
     *
     * @param id the unique identifier of the user
     * @return the updated {@link UserDto} for the unbanned user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    UserDto unban(@NotNull UUID id);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the {@link UserDto} for the user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    UserDto getUser(@NotNull UUID id);

    /**
     * Searches users based on the provided criteria, supporting pagination and filtering.
     *
     * @param criteria the {@link UserSearchCriteria} defining the filters and pagination options
     * @return a {@link Page<UserDto>} containing user search results
     * @throws pl.edu.zut.app.parking.auth.exceptions.InvalidSearchCriteriaException if the search criteria are invalid
     */
    Page<UserDto> searchUsers(@NotNull UserSearchCriteria criteria);

    /**
     * Changes the password for the user identified by the given ID.
     *
     * @param request the {@link ChangePasswordRequest} containing the user ID and new password
     * @throws pl.edu.zut.app.parking.auth.exceptions.ForbiddenException if old password is incorrect
     * @throws AccessDeniedException if a user is not authorized to change the password
     */
    void changePassword(ChangePasswordRequest request);


    /**
     * Changes the password for the user identified by the given ID.
     *
     * @param id the unique identifier of the user whose password is to be changed
     * @param password the new password to be set for the user
     */
    void changePassword(UUID id, String password);


}
