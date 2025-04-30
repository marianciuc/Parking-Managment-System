package pl.edu.zut.app.parking.auth.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import pl.edu.zut.app.parking.auth.entities.User;
import pl.edu.zut.app.parking.auth.exceptions.InvalidRecoveryCodeException;
import pl.edu.zut.app.parking.auth.exceptions.UserNotFoundException;
import pl.edu.zut.app.parking.auth.kafka.PasswordRecoveryMessageProducer;
import pl.edu.zut.app.parking.auth.services.UserService;
import pl.edu.zut.app.parking.auth.utils.ResetPasswordCodeGenerator;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceImplTest {

    @Mock
    private UserRepositoryServiceImpl userRepositoryService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private PasswordRecoveryMessageProducer passwordRecoveryMessageProducer;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordRecoveryServiceImpl passwordRecoveryService;

    @Mock
    private ValueOperations<String, String> valueOperations;


    private final String email = "test@example.com";
    private final String recoveryCode = "12345";
    private final String newPassword = "newPassword123";

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setEmail(email);

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void recoveryPassword_shouldSendPasswordRecoveryMessage_whenEmailExists() {
        try (var resetPasswordCodeGeneratorMock = Mockito.mockStatic(ResetPasswordCodeGenerator.class)) {
            when(userRepositoryService.findByEmail(any(String.class))).thenReturn(mockUser);
            resetPasswordCodeGeneratorMock.when(ResetPasswordCodeGenerator::generateVerificationCode).thenReturn(recoveryCode);

            when(redisTemplate.opsForValue()).thenReturn(valueOperations);
            doNothing().when(valueOperations).set(any(String.class), any(String.class), any(Long.class), any(TimeUnit.class));

            String redisKey = "code:" + recoveryCode;

            passwordRecoveryService.recoveryPassword(email);

            Mockito.verify(valueOperations).set(
                    ArgumentMatchers.eq(redisKey),
                    ArgumentMatchers.eq(email),
                    ArgumentMatchers.eq(10L),
                    ArgumentMatchers.eq(TimeUnit.MINUTES));

            Mockito.verify(passwordRecoveryMessageProducer).sendPasswordRecoveryMessage(
                    ArgumentMatchers.any(),
                    ArgumentMatchers.eq(email),
                    ArgumentMatchers.eq(recoveryCode),
                    ArgumentMatchers.eq(10));
        }
    }

    @Test
    void recoveryPassword_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
        when(userRepositoryService.findByEmail(any(String.class))).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> passwordRecoveryService.recoveryPassword(email));
    }

    @Test
    void resetPassword_shouldResetPassword_whenRecoveryCodeIsValid() {
        when(redisTemplate.opsForValue().get("code:" + recoveryCode)).thenReturn(email);
        when(userRepositoryService.findByEmail(email)).thenReturn(mockUser);

        passwordRecoveryService.resetPassword(recoveryCode, newPassword);

        Mockito.verify(userService).changePassword(mockUser.getId(), newPassword);
    }

    @Test
    void resetPassword_shouldThrowInvalidRecoveryCodeException_whenCodeIsNotFoundInRedis() {
        when(redisTemplate.opsForValue().get("code:" + recoveryCode)).thenReturn(null);

        assertThrows(InvalidRecoveryCodeException.class, () -> passwordRecoveryService.resetPassword(recoveryCode, newPassword));
    }

    @Test
    void resetPassword_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
        when(redisTemplate.opsForValue().get("code:" + recoveryCode)).thenReturn(email);
        when(userRepositoryService.findByEmail(email)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> passwordRecoveryService.resetPassword(recoveryCode, newPassword));
    }
}