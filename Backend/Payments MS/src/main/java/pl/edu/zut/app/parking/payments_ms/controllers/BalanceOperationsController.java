package pl.edu.zut.app.parking.payments_ms.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.PaymentRequest;
import pl.edu.zut.app.parking.payments_ms.dto.WithdrawRequest;
import pl.edu.zut.app.parking.payments_ms.services.PaymentService;

@RestController
@RequestMapping("/api/v1/balance/operations")
@RequiredArgsConstructor
public class BalanceOperationsController {

  private final PaymentService paymentService;

  @PostMapping("/accounts/{accountId}/deposit")
  @PreAuthorize("hasAnyRole('DRIVER', 'PARKING_OWNER')")
  public ResponseEntity<String> topUpBalance(
      @Validated @RequestBody PaymentRequest paymentRequest, @PathVariable UUID accountId) {
    return ResponseEntity.ok(paymentService.deposit(paymentRequest, accountId));
  }

  @PostMapping("/accounts/owners/{ownerId}/withdraw")
  @PreAuthorize("hasRole('PARKING_OWNER, ADMIN')")
  public ResponseEntity<Void> withdraw(
      @RequestBody WithdrawRequest withdrawRequest, @PathVariable UUID ownerId) {
    paymentService.withdraw(withdrawRequest, ownerId);
    return ResponseEntity.ok().build();
  }
}
