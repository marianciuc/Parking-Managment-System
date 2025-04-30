package pl.edu.zut.app.parking.auth.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.dto.req.UserRegistrationRequest;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.kafka.RegistrationUserProducer;
import pl.edu.zut.app.parking.auth.services.JWTService;
import pl.edu.zut.app.parking.auth.services.UserRegistrationService;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;

@Service
@Slf4j
public class DriverRegistrationServiceImpl extends UserRegistrationService {

  private final RegistrationUserProducer registrationUserProducer;

  /**
   * Constructor for injecting dependencies.
   *
   * @param userRepositoryService Service for repository-related operations.
   */
  protected DriverRegistrationServiceImpl(
      UserRepositoryService userRepositoryService,
      PasswordEncoder passwordEncoder,
      @Lazy JWTService jwtService,
      RegistrationUserProducer registrationUserProducer) {
    super(userRepositoryService, passwordEncoder, jwtService);
    this.registrationUserProducer = registrationUserProducer;
  }

  @Override
  public void isCanBeCreated(User user) {
    log.info("Driver registration is allowed");
  }

  @Override
  public void setUserType(User user) {
    user.setUserType(UserType.DRIVER);
  }

  @Override
  public void finishRegister(User user, UserRegistrationRequest request) {
    registrationUserProducer.sendDriverRegistrationMessage(
        user.getId(), user.getEmail(), null, null, null, null, null);
  }
}
