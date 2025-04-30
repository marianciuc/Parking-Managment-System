package pl.edu.zut.app.parking.payments_ms.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.dto.external.SessionPaymentOrderDto;
import pl.edu.zut.app.parking.payments_ms.services.SessionPaymentsService;

@RestController
@RequestMapping("/api/v1/session-payments")
@RequiredArgsConstructor
public class SessionPaymentController {

  private final SessionPaymentsService sessionPaymentsService;

  @PostMapping("/{sessionId}/stripe")
  ResponseEntity<String> createStripeSessionPayment(
      @PathVariable UUID sessionId, @RequestBody SessionPaymentOrderDto req) {
    return ResponseEntity.status(201)
        .body(sessionPaymentsService.createSessionPaymentIntent(sessionId, req));
  }

  @PostMapping("/{sessionId}")
  ResponseEntity<TransactionDto> processSessionPayment(
      @PathVariable UUID sessionId,
      @RequestBody SessionPaymentOrderDto req,
      @RequestParam UUID carOwnerId) {
    return ResponseEntity.ok(
        sessionPaymentsService.processSessionPayment(sessionId, req, carOwnerId));
  }
}
