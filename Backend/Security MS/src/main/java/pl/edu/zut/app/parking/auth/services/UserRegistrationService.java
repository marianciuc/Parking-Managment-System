package pl.edu.zut.app.parking.auth.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.zut.app.parking.auth.dto.common.AuthenticationTokens;
import pl.edu.zut.app.parking.auth.dto.req.UserRegistrationRequest;
import pl.edu.zut.app.parking.auth.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.enums.Possibilities;
import pl.edu.zut.app.parking.auth.exceptions.EmailAlreadyExistsException;
import pl.edu.zut.app.parking.auth.exceptions.UserRegistrationException;
import pl.edu.zut.app.parking.auth.services.UserRepositoryService;


/**
 * Abstract service class responsible for handling user registration logic.
 * It validates user input, performs checks for duplicate email and username,
 * and delegates repository operations to the {@code UserRepositoryService}.
 * The implementation of this class should define how user registration is
 * executed based on specific requirements.
 */
@Slf4j
public abstract class UserRegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    protected final UserRepositoryService userRepositoryService;


    protected UserRegistrationService(UserRepositoryService userRepositoryService, PasswordEncoder passwordEncoder,
                                      JWTService jwtService) {
        this.userRepositoryService = userRepositoryService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user in the system based on the provided user registration request.
     *
     * @param request the user registration request containing username, password, email,
     * @return the generated pair of authentication tokens for the successfully registered user
     * @throws EmailAlreadyExistsException if the provided email already exists in the system
     */
    public AuthenticationTokens registerUser(UserRegistrationRequest request) {
        validateInput(request);

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .recordStatus(AbstractBaseEntity.RecordStatus.ACTIVE)
                .build();

        setUserType(user);
        user.setUserPossibilities(Possibilities.getPossibilitiesByUserType(user.getUserType()));

        isCanBeCreated(user);

        User registeredUser = userRepositoryService.save(user);
        finishRegister(registeredUser, request);
        return jwtService.generateAuthenticationTokens(registeredUser);
    }


    /**
     * Validates whether a given user can be created in the system.
     *
     * This method should be implemented to handle specific conditions that determine
     * whether the creation of the provided user is permissible. The implementation may
     * involve checking user type, attributes, constraints, or other business logic.
     *
     * @param user the user entity to be validated for creation
     * @throws UserRegistrationException if the provided user entity does not meet the criteria
     *                                    required for creation
     */
    public abstract void isCanBeCreated(User user) throws UserRegistrationException;

    public abstract void setUserType(User user);

    public abstract void finishRegister(User user, UserRegistrationRequest request);

    protected void validateInput(UserRegistrationRequest request) {
        if (userRepositoryService.existsByEmail(request.email())) {
            log.error("Registration failed. Email {} is already registered.", request.email());
            throw new EmailAlreadyExistsException(request.email());
        }
    }
}
