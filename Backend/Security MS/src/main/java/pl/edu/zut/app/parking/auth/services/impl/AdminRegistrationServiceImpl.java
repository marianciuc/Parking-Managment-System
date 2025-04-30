package pl.edu.zut.app.parking.auth.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.dto.req.UserRegistrationRequest;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.exceptions.ForbiddenUserRegistrationException;
import pl.edu.zut.app.parking.auth.exceptions.UnauthenticatedUserRegistrationException;
import pl.edu.zut.app.parking.auth.exceptions.UserRegistrationException;
import pl.edu.zut.app.parking.auth.kafka.RegistrationUserProducer;
import pl.edu.zut.app.parking.auth.security.utils.SecurityUtils;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.UserRegistrationService;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;

@Slf4j
@Service
public class AdminRegistrationServiceImpl extends UserRegistrationService {

  private final RegistrationUserProducer registrationUserProducer;

  protected AdminRegistrationServiceImpl(
          UserRepositoryService userRepositoryService,
          PasswordEncoder passwordEncoder,
          JWTService jwtService, RegistrationUserProducer registrationUserProducer) {
    super(userRepositoryService, passwordEncoder, jwtService);
    this.registrationUserProducer = registrationUserProducer;
  }

  /**
   * Validates whether a given user can be created in the system.
   *
   * This method should be implemented to handle specific conditions that determine whether the
   * creation of the provided user is permissible. The implementation may involve checking user
   * type, attributes, constraints, or other business logic.
   *
   * @param user the user entity to be validated for creation
   * @throws UserRegistrationException if the provided user entity does not meet the criteria
   *     required for creation
   */
  @Override
  public void isCanBeCreated(User user) throws UserRegistrationException {
    boolean hasAdmin = super.userRepositoryService.existsByUserType(UserType.ADMIN);

    if (!hasAdmin) {
      log.info(
          "No admin found in the system. Bypassing authentication checks for first admin creation.");
      return;
    }

    log.info("Validating user for registration: {}", user);
    User authenticatedUser = (User) SecurityUtils.extractUser();
    if (authenticatedUser == null) {
      throw new UnauthenticatedUserRegistrationException("User not authenticated");
    }

    if (authenticatedUser.getAuthorities().stream()
        .noneMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
      throw new ForbiddenUserRegistrationException("Only admin can register new admin");
    }
  }

  @Override
  public void setUserType(User user) {
    log.info("Setting user type to ADMIN");
    user.setUserType(UserType.ADMIN);
  }

  @Override
  public void finishRegister(User user, UserRegistrationRequest request) {
    log.info("Finishing registration for user: {}", user);
    registrationUserProducer.sendAdministratorRegistrationMessage(user.getId(), user.getEmail());
  }
}
