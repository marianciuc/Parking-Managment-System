package pl.edu.zut.app.parking.payments_ms.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.KYC;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;

@RestController
@RequestMapping("/api/v1/accounts-balance")
@RequiredArgsConstructor
public class AccountBalanceController {

  private final AccountBalanceService accountBalanceService;

  @GetMapping("/owned/{owenerId}/currency")
  public ResponseEntity<String> getAccountCurrency(@PathVariable UUID owenerId) {
    return ResponseEntity.ok(accountBalanceService.getOwnerCurrency(owenerId).getCode());
  }

  @PatchMapping("/owned/{owenerId}/currency")
  public ResponseEntity<String> updateAccountCurrency(
      @PathVariable UUID owenerId, @RequestBody String currency) {
    return ResponseEntity.ok(
        accountBalanceService.updateOwnerCurrency(owenerId, currency).getCode());
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<AccountBalanceDto> getConvertedAccountBalance(
      @PathVariable UUID accountId,
      @RequestParam(required = false, defaultValue = "false") Boolean converted) {
    return ResponseEntity.ok(accountBalanceService.getAccountBalance(accountId, converted));
  }

  @GetMapping("/owned/{ownerId}")
  public ResponseEntity<AccountBalanceDto> getConvertedOwnerAccountBalance(
      @PathVariable UUID ownerId,
      @RequestParam(required = false, defaultValue = "false") Boolean converted) {
    return ResponseEntity.ok(accountBalanceService.getOwnerAccountBalance(ownerId, converted));
  }

  @PatchMapping("/owned/{ownerId}/verify")
  public ResponseEntity<AccountBalanceDto> verifyAccount(
      @PathVariable UUID ownerId, @RequestBody KYC kyc) {
    return ResponseEntity.ok(accountBalanceService.verifyUserAccount(ownerId, kyc));
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Page<AccountBalanceDto>> findAccounts(
      @RequestParam(required = false, defaultValue = "10") Integer size,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "ASC") String sort,
      @RequestParam(required = false, defaultValue = "id") String direction,
      @RequestParam(required = false) String accountId,
      @RequestParam(required = false, defaultValue = "false") Boolean converted,
      @RequestParam(required = false) String ownerId,
      @RequestParam(required = false) Boolean verified,
      @RequestParam(required = false) String accountType,
      @RequestParam(required = false) String stripeAccountId) {
    return ResponseEntity.ok(
        accountBalanceService.findAccountBalances(
            size,
            page,
            sort,
            direction,
            ownerId,
            verified,
            accountId,
            accountType,
            converted,
            stripeAccountId));
  }
}
