package pl.edu.zut.app.parking.parking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.zut.app.parking.parking.config.FeignClientConfig;
import pl.edu.zut.app.parking.parking.dto.TariffDto;

import java.util.UUID;

@FeignClient(name = "payment-service", configuration = FeignClientConfig.class)
public interface PaymentClient {

    @GetMapping("/api/v1/accounts-balance/owned/{owenerId}/currency")
    ResponseEntity<String> getAccountCurrency(@PathVariable UUID owenerId);
}
