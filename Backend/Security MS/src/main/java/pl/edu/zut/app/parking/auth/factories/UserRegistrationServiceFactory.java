package pl.edu.zut.app.parking.auth.factories;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.edu.zut.app.parking.auth.enums.UserType;
import pl.edu.zut.app.parking.auth.services.UserRegistrationService;
import pl.edu.zut.app.parking.auth.services.impl.AdminRegistrationServiceImpl;
import pl.edu.zut.app.parking.auth.services.impl.DriverRegistrationServiceImpl;
import pl.edu.zut.app.parking.auth.services.impl.ParkingOwnersRegistrationServiceImpl;

/**
 * Factory class for creating instances of UserRegistrationService based on UserType.
 */
@Component
@AllArgsConstructor
public class UserRegistrationServiceFactory {

    private final DriverRegistrationServiceImpl customerRegistrationService;
    private final ParkingOwnersRegistrationServiceImpl parkingRegistrationService;
    private final AdminRegistrationServiceImpl adminRegistrationService;

    /**
     * Returns the appropriate UserRegistrationService implementation based on the provided UserType.
     *
     * @param userType the type of user for which the registration service is required
     * @return the corresponding UserRegistrationService implementation
     * @throws IllegalArgumentException if the provided UserType is unknown
     * @see UserRegistrationService
     * @see UserType
     */
    public UserRegistrationService getRegistrationService(UserType userType) {
        return switch (userType) {
            case DRIVER -> customerRegistrationService;
            case PARKING_OWNER -> parkingRegistrationService;
            case ADMIN -> adminRegistrationService;
            default -> throw new IllegalArgumentException("Unknown user type: " + userType);
        };
    }

}
