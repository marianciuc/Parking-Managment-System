package pl.edu.zut.app.parking.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.InvalidRecoveryCodeException;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;
import pl.edu.zut.app.parking.auth.kafka.PasswordRecoveryMessageProducer;
import pl.edu.zut.app.parking.auth.services.PasswordRecoveryService;
import pl.edu.zut.app.parking.auth.services.UserService;
import pl.edu.zut.app.parking.auth.utils.ResetPasswordCodeGenerator;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private final UserRepositoryServiceImpl userRepositoryService;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordRecoveryMessageProducer passwordRecoveryMessageProducer;
    private final UserService userService;

    private static final int CODE_EXPIRATION_MINUTES = 10;
    private static final TimeUnit CODE_EXPIRATION_TIME_UNIT = TimeUnit.MINUTES;

    /**
     * Initiates the password recovery process for the user associated with the provided email address.
     * This method generates 5-digit code, save it in a redis database and send code to notification service.
     *
     * @param email the email address of the user requesting password recovery
     * @throws UserNotFoundException if a user does not exist with provided email
     */
    @Override
    public void recoveryPassword(String email) {
        User user = userRepositoryService.findByEmail(email);

        String code = ResetPasswordCodeGenerator.generateVerificationCode();
        String key = generateKeyForCode(code);
        saveCodeAndEmailToRedis(key, user.getEmail());
        passwordRecoveryMessageProducer.sendPasswordRecoveryMessage(user.getId(), user.getEmail(), code, CODE_EXPIRATION_MINUTES);
    }

    /**
     * Resets the password for the user by validating the provided recovery code and setting the new password.
     *
     * @param code     the unique recovery code provided to the user during the password recovery process
     * @param password the new password to be set for the user
     * @throws InvalidRecoveryCodeException if the provided recovery code is invalid or expired
     * @throws UserNotFoundException        if no user is associated with the provided recovery code
     */
    @Override
    public void resetPassword(String code, String password) {
        String key = generateKeyForCode(code);
        String storedEmail = getEmailForCodeFromRedis(key);

        if (storedEmail == null) {
            throw new InvalidRecoveryCodeException("Invalid recovery code");
        }

        User user = userRepositoryService.findByEmail(storedEmail);
        userService.changePassword(user.getId(), password);
    }


    private String generateKeyForCode(String code) {
        return "code:" + code;
    }


    private String getEmailForCodeFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void saveCodeAndEmailToRedis(String key, String email) {
        redisTemplate.opsForValue().set(key, email, CODE_EXPIRATION_MINUTES, CODE_EXPIRATION_TIME_UNIT);
    }
}
