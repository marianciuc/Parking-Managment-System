package pl.edu.zut.app.parking.tariffs.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.zut.app.parking.tariffs.config.FeignClientConfig;
import pl.edu.zut.app.parking.tariffs.dto.external.responces.ExchangeRateResDto;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment-service", configuration = FeignClientConfig.class)
public interface PaymentClient {

    @GetMapping("/api/v1/exchange")
    ResponseEntity<ExchangeRateResDto> exchange(@RequestParam String from, @RequestParam String to,
                                                @RequestParam BigDecimal amount);

  @GetMapping("/api/v1/accounts-balance/owned/{owenerId}/currency")
  ResponseEntity<String> getAccountCurrency(@PathVariable UUID owenerId);
}
