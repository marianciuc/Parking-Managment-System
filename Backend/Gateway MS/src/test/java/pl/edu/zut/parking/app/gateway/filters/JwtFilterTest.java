package pl.edu.zut.parking.app.gateway.filters;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import pl.edu.zut.parking.app.gateway.dto.Token;
import pl.edu.zut.parking.app.gateway.services.JwtService;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private ServerHttpRequest request;

    @InjectMocks
    private JwtFilter jwtFilter;

    private Token mockToken;

    @BeforeEach
    void setUp() {
        mockToken = new Token(
                UUID.randomUUID(),
                "user123",
                "issuer",
                List.of("ROLE_USER", "ROLE_ADMIN"),
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        when(exchange.getRequest()).thenReturn(request);
        lenient().when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void filter_SecurityEndpoint_SkipsTokenValidation() throws Exception {
        // Arrange
        when(request.getURI()).thenReturn(new URI("/api/v1/security/login"));

        // Act
        Mono<Void> result = jwtFilter.filter(exchange, chain);

        // Assert
        assertNotNull(result);
        verify(jwtService, never()).extractBearerToken(any());
        verify(chain).filter(exchange);
    }

    @Test
    void filter_ValidToken_AddsUserHeaders() throws Exception {
        // Arrange
        when(request.getURI()).thenReturn(new URI("/api/v1/users"));
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer valid-token");
        when(request.getHeaders()).thenReturn(headers);
        when(jwtService.extractBearerToken(any())).thenReturn(Optional.of("valid-token"));
        when(jwtService.parseToken("valid-token")).thenReturn(mockToken);

        ServerHttpRequest.Builder requestBuilder = mock(ServerHttpRequest.Builder.class);
        when(request.mutate()).thenReturn(requestBuilder);
        when(requestBuilder.header(anyString(), anyString())).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(request);

        ServerWebExchange.Builder exchangeBuilder = mock(ServerWebExchange.Builder.class);
        when(exchange.mutate()).thenReturn(exchangeBuilder);
        when(exchangeBuilder.request(any(ServerHttpRequest.class))).thenReturn(exchangeBuilder);
        when(exchangeBuilder.build()).thenReturn(exchange);

        // Act
        Mono<Void> result = jwtFilter.filter(exchange, chain);

        // Assert
        assertNotNull(result);
        verify(jwtService).parseToken("valid-token");
        verify(requestBuilder).header("X-User-Id", mockToken.subject());
        verify(requestBuilder).header("X-User-Roles", mockToken.getRolesAsStringList());
        verify(chain).filter(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/v1/users",
            "/api/v1/products",
            "/api/v1/orders"
    })
    void filter_NonSecurityEndpoints_ValidatesToken(String path) throws Exception {
        // Arrange
        when(request.getURI()).thenReturn(new URI(path));
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        when(request.getHeaders()).thenReturn(headers);
        when(jwtService.extractBearerToken(any())).thenReturn(Optional.empty());

        // Act
        Mono<Void> result = jwtFilter.filter(exchange, chain);

        // Assert
        assertNotNull(result);
        verify(jwtService).extractBearerToken(any());
        verify(chain).filter(exchange);
    }

    @Test
    void filter_InvalidToken_ThrowsException() throws Exception {
        // Arrange
        when(request.getURI()).thenReturn(new URI("/api/v1/users"));
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer invalid-token");
        when(request.getHeaders()).thenReturn(headers);
        when(jwtService.extractBearerToken(any())).thenReturn(Optional.of("invalid-token"));
        when(jwtService.parseToken("invalid-token")).thenThrow(new IllegalArgumentException("Invalid token"));

        // Act
        Mono<Void> result = jwtFilter.filter(exchange, chain);

        // Assert
        assertThrows(RuntimeException.class, () -> result.block());
    }
}