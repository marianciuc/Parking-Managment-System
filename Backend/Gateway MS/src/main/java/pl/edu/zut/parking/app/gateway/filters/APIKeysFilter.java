package pl.edu.zut.parking.app.gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import pl.edu.zut.parking.app.gateway.dto.ApiKeyDto;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class APIKeysFilter implements GlobalFilter, Ordered {

    private static final String INVALID_API_KEY_MESSAGE = "Invalid API key";
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-Api-Key");
        if (apiKey != null && !apiKey.isEmpty()) {
            log.info("Validating API key: {}", apiKey);

            return webClientBuilder.baseUrl("lb://AUTH-SERVICE").build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/api-keys/validate/" + apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(ApiKeyDto.class)
                    .flatMap(apiKeyDto -> {
                        log.info("API key is valid for parking id: {}, scopes: {}", apiKeyDto.parkingId(), apiKeyDto.scopes());
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Parking-Id", apiKeyDto.parkingId().toString())
                                .header("X-Scope", String.join(",", apiKeyDto.scopes()))
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    })
                    .onErrorResume(e -> {
                        log.error("Stack {}", e.getStackTrace());
                        log.error("Error during API key validation: {}", e.getMessage(), e);
                        return Mono.error(new RuntimeException(INVALID_API_KEY_MESSAGE));
                    });
        }
        log.info("API key is not present or empty");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
