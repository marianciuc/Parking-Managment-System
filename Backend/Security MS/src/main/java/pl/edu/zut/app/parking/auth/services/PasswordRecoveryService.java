package pl.edu.zut.app.parking.auth.services;

/**
 * Service interface responsible for handling password recovery operations,
 * including initiating password recovery and resetting the password for users.
 */
public interface PasswordRecoveryService {

    /**
     * Initiates the password recovery process for the user associated with the provided email address.
     * This method generates 5-digit code, save it in a redis database and send code to notification service.
     *
     * @param email the email address of the user requesting password recovery
     * @throws pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException if a user does not exist with provided email
     */
    void recoveryPassword(String email);


    /**
     * Resets the password for the user by validating the provided recovery code and setting the new password.
     *
     * @param code the unique recovery code provided to the user during the password recovery process
     * @param password the new password to be set for the user
     * @throws pl.edu.zut.app.parking.auth.exceptions.InvalidRecoveryCodeException if the provided recovery code is invalid or expired
     * @throws pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException if no user is associated with the provided recovery code
     */
    void resetPassword(String code, String password);
}
