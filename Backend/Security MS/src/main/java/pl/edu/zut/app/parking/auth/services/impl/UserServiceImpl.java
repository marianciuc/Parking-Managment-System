package pl.edu.zut.app.parking.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.dto.ChangePasswordRequest;
import pl.edu.zut.app.parking.auth.dto.UserDto;
import pl.edu.zut.app.parking.auth.dto.UserSearchCriteria;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.ForbiddenException;
import pl.edu.zut.app.parking.auth.exceptions.InvalidSearchCriteriaException;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;
import pl.edu.zut.app.parking.auth.security.utils.SecurityUtils;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;
import pl.edu.zut.app.parking.auth.services.UserService;
import pl.edu.zut.app.parking.auth.specifications.UserSpecifications;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryService userRepositoryService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Bans the user identified by the given ID.
     *
     * @param id the unique identifier of the user
     * @return the updated {@link UserDto} for the banned user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    @Override
    public UserDto ban(UUID id) {
        User user = userRepositoryService.findById(id);
        user.setRecordStatus(User.RecordStatus.BANNED);
        return UserDto.fromEntity(userRepositoryService.save(user));
    }

    /**
     * Removes a ban for the user identified by the given ID.
     *
     * @param id the unique identifier of the user
     * @return the updated {@link UserDto} for the unbanned user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    @Override
    public UserDto unban(UUID id) {
        User user = userRepositoryService.findById(id);
        user.setRecordStatus(User.RecordStatus.ACTIVE);
        return UserDto.fromEntity(userRepositoryService.save(user));
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the {@link UserDto} for the user
     * @throws UserNotFoundException if the user with the specified ID does not exist
     */
    @Override
    public UserDto getUser(UUID id) {
        return UserDto.fromEntity(userRepositoryService.findById(id));
    }

    /**
     * Searches users based on the provided criteria, supporting pagination and filtering.
     *
     * @param criteria the {@link UserSearchCriteria} defining the filters and pagination options
     * @return a {@link Page <UserDto>} containing user search results
     * @throws InvalidSearchCriteriaException if the search criteria are invalid
     */
    @Override
    public Page<UserDto> searchUsers(UserSearchCriteria criteria) {
        Specification<User> specification = Specification.where(UserSpecifications.withEmail(criteria.email()))
                .and(UserSpecifications.withRecordStatus(criteria.recordStatus()))
                .and(UserSpecifications.withUserType(criteria.userType()));

        return userRepositoryService.findAll(specification, Pageable.ofSize(criteria.size()).withPage(criteria.page()))
                .map(UserDto::fromEntity);
    }

    /**
     * Changes the password for the authenticated user.
     *
     * @param request the {@link ChangePasswordRequest} containing the user ID and new password
     * @throws ForbiddenException    if old password is incorrect
     * @throws AccessDeniedException if a user is not authorized to change the password
     */
    @Override
    public void changePassword(ChangePasswordRequest request) {
        UUID userId = SecurityUtils.extractUser().getId();

        User user = userRepositoryService.findById(userId);
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            throw new ForbiddenException("Old password is incorrect");
        }

        changePassword(userId, request.newPassword());
    }

    /**
     * Changes the password for the user identified by the given ID.
     *
     * @param id       the unique identifier of the user whose password is to be changed
     * @param password the new password to be set for the user
     */
    @Override
    public void changePassword(UUID id, String password) {
        User user = userRepositoryService.findById(id);
        String newPasswordHash = passwordEncoder.encode(password);
        user.setPasswordHash(newPasswordHash);
        userRepositoryService.save(user);
    }
}
