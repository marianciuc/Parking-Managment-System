package pl.edu.zut.parking.app.gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pl.edu.zut.parking.app.gateway.dto.Token;
import pl.edu.zut.parking.app.gateway.services.JwtService;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter implements GlobalFilter, Ordered {

    private static final String INVALID_JWT_TOKEN_MESSAGE = "Invalid JWT token";
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Global Filtering request: {}", exchange.getRequest().getURI().getPath());
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/api/v1/security/") || path.startsWith("/api/oauth2/")) {
            log.info("Skipping JWT filter for path: {}", path);
            return chain.filter(exchange);
        }

        Optional<String> token =
                jwtService.extractBearerToken(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        if (token.isPresent()) {
            try {
                exchange = processToken(exchange, token.get());
            } catch (Exception e) {
                return Mono.error(new RuntimeException(INVALID_JWT_TOKEN_MESSAGE));
            }
        }
        log.info("No JWT token found");
        return chain.filter(exchange);
    }

    private ServerWebExchange processToken(ServerWebExchange exchange, String token) {
        Token parsedToken = jwtService.parseToken(token);
        String userId = parsedToken.subject();
        String roles = parsedToken.getRolesAsStringList();

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId)
                .header("X-User-Roles", roles)
                .build();

        return exchange.mutate().request(mutatedRequest).build();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
