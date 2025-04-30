package pl.edu.zut.app.parking.payments_ms.services.impl;

import com.stripe.exception.StripeException;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.common.BankAccountDto;
import pl.edu.zut.app.parking.payments_ms.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;
import pl.edu.zut.app.parking.payments_ms.entities.BankAccount;
import pl.edu.zut.app.parking.payments_ms.exceptions.BankAccountCreationException;
import pl.edu.zut.app.parking.payments_ms.exceptions.BankAccountNotFoundException;
import pl.edu.zut.app.parking.payments_ms.integrations.StripeIntegrationService;
import pl.edu.zut.app.parking.payments_ms.repositories.BankAccountRepository;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.BankAccountService;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

  private final EntityManager entityManager;
  private final BankAccountRepository bankAccountRepository;
  private final StripeIntegrationService stripeIntegrationService;
  private final AccountBalanceService accountBalanceService;

  @Override
  public BankAccountDto createBankAccount(BankAccountDto bankAccount, UUID userId)
      throws BankAccountCreationException {
    AccountBalanceDto accountBalance = accountBalanceService.getOwnerAccountBalance(userId, false);
    try {
      String pmId =
          stripeIntegrationService.createIbanPaymentMethod(
              accountBalance.stripeAccountId(),
              bankAccount.iban(),
              bankAccount.fullname(),
              accountBalance.preferredCurrency(),
              bankAccount.country());

      BankAccount bankAccountEntity =
          BankAccount.builder()
              .iban(bankAccount.iban())
              .fullname(bankAccount.fullname())
              .bankName(bankAccount.bankName())
              .country(bankAccount.country())
              .paymentMethodId(pmId)
              .accountBalance(entityManager.getReference(AccountBalance.class, accountBalance.id()))
              .build();

      return BankAccountDto.fromEntity(bankAccountRepository.save(bankAccountEntity));
    } catch (StripeException e) {
      throw new BankAccountCreationException(e.getMessage(), e);
    }
  }

  @Override
  public BankAccountDto getBankAccount(UUID bankAccountId) throws BankAccountNotFoundException {
    return BankAccountDto.fromEntity(
        bankAccountRepository
            .findByIdAndRecordStatusIsNot(bankAccountId, AbstractBaseEntity.RecordStatus.DELETED)
            .orElseThrow(
                () ->
                    new BankAccountNotFoundException("Bank account not found: " + bankAccountId)));
  }

  @Override
  public void deleteBankAccount(UUID bankAccountId) throws BankAccountNotFoundException {
    BankAccount bankAccount =
        bankAccountRepository
            .findByIdAndRecordStatusIsNot(bankAccountId, AbstractBaseEntity.RecordStatus.DELETED)
            .orElseThrow(
                () -> new BankAccountNotFoundException("Bank account not found: " + bankAccountId));

    bankAccount.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
    bankAccountRepository.save(bankAccount);
  }

  @Override
  public List<BankAccountDto> getUserBankAccounts(UUID userId) {
    return bankAccountRepository
        .findAllByAccountBalance_OwenerIdAndRecordStatusIsNot(
            userId, AbstractBaseEntity.RecordStatus.DELETED)
        .stream()
        .map(BankAccountDto::fromEntity)
        .toList();
  }
}
