package pl.edu.zut.parking.app.gateway.services.impl;

import com.github.javafaker.Faker;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
@Testcontainers
@SpringBootTest
public class JWTBlackListServiceIntegrationTest {

    private static final Faker faker = new Faker();

    private RedisTemplate<String, String> redisTemplate;
    private JWTBlackListServiceImpl jwtBlackListService;


    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public JWSVerifier testJwsVerifier() throws Exception {
            String testPublicKey = "{\"kty\":\"RSA\",\"e\":\"AQAB\",\"kid\":\"Outl4hhj-Ne-2J1OeK6x2CZUM1RNayGFef677upqJRw\",\"n\":\"vkIo2IdIaARPhoWkPziGJiKsqB-HHhVCrLZO9QORzu1YPgiqj5MDaGqyzHo4EyIPOrIBV9qbl9zfaESuFZXaV00rrDx8drIreE_yNMr_ihdJqyyezsefpuYr_9c1nq7Tzh4l7hkDq3ER1moyx-vdiuwjv5xn7qh45jhNBun8XLUaIN4xvPoH6dYQJjHBuJscryX0OwW3N6LKLCTHk2L7c3TKwrPtwPVN7UL1-_atTDrdWVHL3KqzGup-XnGfAOUiu9sPOXTyFZcggwcjQoUEfdp00mhGFMvUqdZfxts3Jc-wgTOAN0hd_775HKmpq6znVOM33b4JedZD1RT0m9ZTtQ\"}";
            RSAKey parsedRSAKey = RSAKey.parse(testPublicKey);
            return new RSASSAVerifier(parsedRSAKey);
        }
    }

    @BeforeEach
    void setUp() {
        LettuceConnectionFactory connectionFactory =
                new LettuceConnectionFactory(redis.getHost(), redis.getFirstMappedPort());
        connectionFactory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        jwtBlackListService = new JWTBlackListServiceImpl(redisTemplate);
    }



    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7.0"))
            .withExposedPorts(6379);

    @Test
    @DisplayName("Integration test - Block and check token")
    void shouldBlockAndCheckToken() {
        String token = faker.internet().uuid();
        int expirationTime = 1000;

        // Act
        jwtBlackListService.blockToken(token, expirationTime);
        boolean isBlocked = jwtBlackListService.isTokenBlocked(token);

        // Assert
        assertThat(isBlocked).isTrue();
    }

    @Test
    @DisplayName("Integration test - Token expiration")
    void shouldExpireBlockedToken() throws InterruptedException {
        String token = faker.internet().uuid();
        int expirationTime = 100;

        jwtBlackListService.blockToken(token, expirationTime);
        Thread.sleep(200);
        boolean isBlocked = jwtBlackListService.isTokenBlocked(token);

        assertThat(isBlocked).isFalse();
    }




}
