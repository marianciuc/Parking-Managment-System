package pl.edu.zut.parking.app.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@Order(0)
public class PreFlightRequestFilter {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter corsPreFlightGlobalFilter() {
        return (exchange, chain) -> {
            String origin = exchange.getRequest().getHeaders().getOrigin();
            log.info("corsPreFlightGlobalFilter method {}, origin: {}", exchange.getRequest().getMethod(), origin);

            if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
                log.info("OPTIONS request detected");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", origin);
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PATCH, DELETE");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization");
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(exchange);
        };
    }
}