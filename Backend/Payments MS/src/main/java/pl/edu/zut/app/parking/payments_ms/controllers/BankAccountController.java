package pl.edu.zut.app.parking.payments_ms.controllers;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.payments_ms.dto.common.BankAccountDto;
import pl.edu.zut.app.parking.payments_ms.services.BankAccountService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

  private final BankAccountService bankAccountService;

  @PostMapping
  @PreAuthorize("hasRole('PARKING_OWNER')")
  public ResponseEntity<BankAccountDto> createBankAccount(@RequestBody BankAccountDto bankAccount) {
    return ResponseEntity.ok(
        bankAccountService.createBankAccount(
            bankAccount, SecurityContextUtil.extractUserIdFromSecurityContext()));
  }

  // TODO: check if user is owner of the account
  @DeleteMapping("/{bankAccountId}")
  public ResponseEntity<Void> deleteBankAccount(@PathVariable UUID bankAccountId) {
    bankAccountService.deleteBankAccount(bankAccountId);
    return ResponseEntity.ok().build();
  }

  // TODO: check if user is owner of the account or admin
  @GetMapping("/{ownerId}/account/{bankAccountId}")
  public ResponseEntity<BankAccountDto> getBankAccount(@PathVariable UUID bankAccountId, @PathVariable UUID ownerId) {
    return ResponseEntity.ok(bankAccountService.getBankAccount(bankAccountId));
  }

  // TODO: check if the user is a owner of this account or he is an admin
  @GetMapping("/{ownerId}")
  public ResponseEntity<List<BankAccountDto>> getBankAccounts(@PathVariable UUID ownerId) {
    return ResponseEntity.ok(bankAccountService.getUserBankAccounts(ownerId));
  }
}
