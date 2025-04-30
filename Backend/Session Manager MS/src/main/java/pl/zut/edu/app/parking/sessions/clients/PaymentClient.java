package pl.zut.edu.app.parking.sessions.clients;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zut.edu.app.parking.sessions.config.FeignClientConfig;
import pl.zut.edu.app.parking.sessions.dto.Payment;

@FeignClient(name = "payment-service", configuration = FeignClientConfig.class)
public interface PaymentClient {

  @GetMapping("/api/v1/session-payments/{sessionId}")
  ResponseEntity<Boolean> paySession(@PathVariable UUID sessionId, @RequestBody Payment payment);

  @GetMapping("/api/v1/transactions/calculate/{sessionId}/paid")
  ResponseEntity<BigDecimal> calculateTotalPaidAmount(@PathVariable UUID sessionId, @RequestParam String currency);
}
