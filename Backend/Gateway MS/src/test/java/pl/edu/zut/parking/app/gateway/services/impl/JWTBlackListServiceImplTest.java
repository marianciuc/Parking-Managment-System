package pl.edu.zut.parking.app.gateway.services.impl;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTBlackListServiceImplTest {

    private static final Faker faker = new Faker();

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    @InjectMocks
    private JWTBlackListServiceImpl jwtBlackListService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps); // Example
        lenient().doNothing().when(valueOps).set(anyString(), anyString());
    }

    @Nested
    @DisplayName("blockToken tests")
    class BlockTokenTests {

        @Test
        @DisplayName("Should successfully block valid token")
        void shouldBlockValidToken() {
            // Arrange
            String token = faker.internet().uuid();
            long expirationTime = faker.number().numberBetween(1, 1000000);
            String expectedKey = "blacklist:" + token;

            // Act
            jwtBlackListService.blockToken(token, (int) expirationTime);

            // Assert
            verify(valueOps).set(eq(expectedKey), eq(""), eq(expirationTime), eq(TimeUnit.MILLISECONDS));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should throw exception for blank tokens")
        void shouldThrowExceptionForBlankTokens(String blankToken) {
            // Act & Assert
            assertThatThrownBy(() ->
                    jwtBlackListService.blockToken(blankToken, 1000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Token cannot be null or empty.");
        }

        @Test
        @DisplayName("Should throw exception for null token")
        void shouldThrowExceptionForNullToken() {
            // Act & Assert
            assertThatThrownBy(() ->
                    jwtBlackListService.blockToken(null, 1000))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Token cannot be null or empty.");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -100})
        @DisplayName("Should throw exception for invalid expiration times")
        void shouldThrowExceptionForInvalidExpirationTimes(int invalidTime) {
            // Arrange
            String token = faker.internet().uuid();

            // Act & Assert
            assertThatThrownBy(() ->
                    jwtBlackListService.blockToken(token, invalidTime))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Expiration time must be greater than zero.");
        }
    }

    @Nested
    @DisplayName("isTokenBlocked tests")
    class IsTokenBlockedTests {

        @ParameterizedTest
        @MethodSource("provideTokensAndExpectedResults")
        @DisplayName("Should correctly check if token is blocked")
        void shouldCheckIfTokenIsBlocked(String token, Boolean redisResponse, boolean expected) {
            // Arrange
            lenient().when(redisTemplate.hasKey(anyString())).thenReturn(redisResponse);

            // Act
            boolean result = jwtBlackListService.isTokenBlocked(token);

            // Assert
            assertThat(result).isEqualTo(expected);
        }

        private static Stream<Arguments> provideTokensAndExpectedResults() {
            return Stream.of(
                    Arguments.of(faker.internet().uuid(), true, true),
                    Arguments.of(faker.internet().uuid(), false, false),
                    Arguments.of("", null, false),
                    Arguments.of(" ", null, false),
                    Arguments.of(null, null, false)
            );
        }
    }

}