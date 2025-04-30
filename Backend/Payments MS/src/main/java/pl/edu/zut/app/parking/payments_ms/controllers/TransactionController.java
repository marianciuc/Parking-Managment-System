package pl.edu.zut.app.parking.payments_ms.controllers;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.services.StatsService;
import pl.edu.zut.app.parking.payments_ms.services.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;
  private final StatsService statsService;

  @GetMapping("/search")
  public ResponseEntity<Page<TransactionDto>> searchTransactions(
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
      @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
      @RequestParam(required = false) UUID transactionId,
      @RequestParam(required = false) UUID bankAccountId,
      @RequestParam(required = false) TransactionStatus status,
      @RequestParam(required = false) UUID sourceAccountId,
      @RequestParam(required = false) UUID destinationAccountId,
      @RequestParam(required = false) UUID parkingId,
      @RequestParam(required = false) UUID sessionId,
      @RequestParam(required = false) String stripePaymentId,
      @RequestParam(required = false, defaultValue = "false") Boolean converted,
      @RequestParam(required = false, defaultValue = "USD") Currency currency,
      @RequestParam(required = false) TransactionType transactionType,
      @RequestParam(required = false) String subscriptionOrderId) {
    return ResponseEntity.ok(
        transactionService.findTransactions(
            size,
            page,
            sort,
            direction,
            transactionId,
            bankAccountId,
            status,
            sourceAccountId,
            destinationAccountId,
            parkingId,
            sessionId,
            stripePaymentId,
            converted,
            currency,
            transactionType,
            subscriptionOrderId));
  }

  @GetMapping("/{transactionId}")
  public ResponseEntity<TransactionDto> getTransaction(@PathVariable UUID transactionId) {
    return ResponseEntity.ok(transactionService.getTransaction(transactionId));
  }

  @GetMapping("/calculate/{sessionId}/paid")
  public ResponseEntity<BigDecimal> paid(
      @PathVariable UUID sessionId, @RequestParam Currency currency) {
    return ResponseEntity.ok(statsService.calculateSessionTotalAmount(sessionId, currency));
  }
}
