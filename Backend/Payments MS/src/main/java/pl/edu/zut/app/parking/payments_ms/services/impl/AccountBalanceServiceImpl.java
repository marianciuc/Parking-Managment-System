package pl.edu.zut.app.parking.payments_ms.services.impl;

import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.payments_ms.dto.KYC;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.entities.AccountBalance;
import pl.edu.zut.app.parking.payments_ms.entities.ConversionDetails;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.exceptions.*;
import pl.edu.zut.app.parking.payments_ms.integrations.StripeIntegrationService;
import pl.edu.zut.app.parking.payments_ms.kafka.ChangeCurrencyMessageProducer;
import pl.edu.zut.app.parking.payments_ms.repositories.AccountBalanceRepository;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.ExchangeService;
import pl.edu.zut.app.parking.payments_ms.specifications.AccountBalanceSpecifications;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountBalanceServiceImpl implements AccountBalanceService {

  private static final Currency SYSTEM_CURRENCY = Currency.USD;

  private final AccountBalanceRepository accountBalanceRepository;
  private final ExchangeService exchangeService;
  private final StripeIntegrationService stripeIntegrationService;
  private final ChangeCurrencyMessageProducer changeCurrencyMessageProducer;

  @Override
  public void updateAccountBalance(TransactionDto transaction)
      throws InsufficientFundsException, AccountBalanceInvalidArgumentsException {
    log.info("Updating account balance associated with transaction {}", transaction.id());

    switch (transaction.type()) {
      case WITHDRAWAL -> updateWithdrawals(transaction);
      case EXTERNAL_DEPOSIT -> updateDeposits(transaction);
      case SESSION_EXTERNAL_PAYMENT -> updateExternalSessionPayment(transaction);
      case SESSIONS_PAYMENT -> updateSessionPayment(transaction);
      case INTERNAL_TRANSFER -> updateInternalTransfers(transaction);
    }
  }

  private void updateInternalTransfers(TransactionDto transaction)
      throws InsufficientFundsException, AccountBalanceInvalidArgumentsException {
    if (transaction.status().equals(TransactionStatus.PENDING)) {
      AccountBalance sourceAccount = getAccountOrThrow(transaction.sourceAccount().id());
      AccountBalance destinationAccount = getAccountOrThrow(transaction.destinationAccount().id());

      sourceAccount.withdraw(transaction.conversionDetails().destinationAmount());
      destinationAccount.deposit(transaction.conversionDetails().destinationAmount());

      accountBalanceRepository.save(sourceAccount);
      accountBalanceRepository.save(destinationAccount);
    }
  }

  private void updateDeposits(TransactionDto transaction) throws InsufficientFundsException {
    if (transaction.status().equals(TransactionStatus.COMPLETED)) {
      AccountBalance accountBalance = getAccountOrThrow(transaction.sourceAccount().id());
      accountBalance.deposit(transaction.conversionDetails().destinationAmount());
      accountBalanceRepository.save(accountBalance);
    }
  }

  private AccountBalance getAccountOrThrow(UUID accountId) {
    return accountBalanceRepository
        .findById(accountId)
        .orElseThrow(() -> new AccountBalanceNotFoundException(accountId));
  }

  private void updateSessionPayment(TransactionDto transaction)
      throws InsufficientFundsException, AccountBalanceInvalidArgumentsException {
    if (transaction.status().equals(TransactionStatus.COMPLETED)) {
      AccountBalance sourseAccount = getAccountOrThrow(transaction.sourceAccount().id());
      AccountBalance destinationAccount = getAccountOrThrow(transaction.destinationAccount().id());

      sourseAccount.withdraw(transaction.conversionDetails().destinationAmount());
      destinationAccount.deposit(transaction.conversionDetails().destinationAmount());
      accountBalanceRepository.save(sourseAccount);
      accountBalanceRepository.save(destinationAccount);
    }
  }

  private void updateExternalSessionPayment(TransactionDto transaction)
      throws InsufficientFundsException {
    if (transaction.status().equals(TransactionStatus.COMPLETED)) {
      log.info("Updating account balance associated with external session payment {}", transaction.id());
      AccountBalance destinationAccount = getAccountOrThrow(transaction.destinationAccount().id());

      destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.conversionDetails().destinationAmount()));

      AccountBalance accountBalance = accountBalanceRepository.save(destinationAccount);
      log.info("Successfully updated account balance associated with external session payment {}, new balance {}",
              transaction.id(), accountBalance.getBalance());
    }
  }

  private void updateWithdrawals(TransactionDto transaction)
      throws InsufficientFundsException, AccountBalanceInvalidArgumentsException {
    AccountBalance sourseAccount = getAccountOrThrow(transaction.sourceAccount().id());

    if (transaction.status().equals(TransactionStatus.PENDING)) {
      sourseAccount.withdraw(transaction.conversionDetails().destinationAmount());
      accountBalanceRepository.save(sourseAccount);
    } else if (transaction.status().equals(TransactionStatus.FAILED)) {
      sourseAccount.deposit(transaction.conversionDetails().destinationAmount());
    }
  }

  @Override
  public void createAccountBalance(UUID ownerId, AccountType accountType) {
    if (accountBalanceRepository.existsByOwenerId(ownerId)) {
      log.warn("Account balance with for owner {} already exists", ownerId);
      throw new AccountBalanceAlreadyExistsException(
          "Account balance for owner " + ownerId + " already exists");
    }

    AccountBalance accountBalance =
        AccountBalance.builder()
            .ownerId(ownerId)
            .accountType(accountType)
            .balance(BigDecimal.ZERO)
            .bankAccounts(new ArrayList<>())
            .systemCurrency(SYSTEM_CURRENCY)
            .isVerifiedAccount(false)
            .preferredCurrency(Currency.USD)
            .build();

    accountBalanceRepository.save(accountBalance);
  }

  public void createStripeAccount(UUID ownerId, String country, String email) {
    AccountBalance accountBalance =
        accountBalanceRepository
            .findAccountBalanceById(ownerId)
            .orElseThrow(() -> new AccountBalanceNotFoundException(ownerId));

    try {
      accountBalance.setStripeAccountId(stripeIntegrationService.createAccount(email, country));
      accountBalanceRepository.save(accountBalance);
      log.info("Stripe account for owner {} created", ownerId);
    } catch (StripeException e) {
      throw new AccountCreationException(e.getMessage());
    }
  }

  @Override
  public Currency getOwnerCurrency(UUID ownerId) {
    return accountBalanceRepository
        .findAccountBalancesByOwenerId(ownerId)
        .orElseThrow(
            () ->
                new AccountBalanceNotFoundException(
                    "Account balance with id " + ownerId + " not found"))
        .getPreferredCurrency();
  }

  @Override
  public Currency updateOwnerCurrency(UUID ownerId, String currency) {
    AccountBalance accountBalance =
        accountBalanceRepository
            .findAccountBalancesByOwenerId(ownerId)
            .orElseThrow(
                () ->
                    new AccountBalanceNotFoundException(
                        "Account balance with id " + ownerId + " not found"));

    Currency preferredCurrency = Currency.fromCode(currency);

    if (preferredCurrency.equals(accountBalance.getPreferredCurrency())) {
      log.warn("Account balance with id {} already has currency {}", ownerId, currency);
      throw new AccountBalanceInvalidArgumentsException(
          "Account balance with id " + ownerId + " already has currency " + currency);
    }

    accountBalance.setPreferredCurrency(Currency.fromCode(currency));
    accountBalanceRepository.save(accountBalance);
    changeCurrencyMessageProducer.sendChangeCurrencyMessage(ownerId, currency);
    return accountBalance.getPreferredCurrency();
  }

  @Override
  public AccountBalanceDto getOwnerAccountBalance(UUID ownerId, Boolean converted) {
    AccountBalance accountBalance =
        accountBalanceRepository
            .findAccountBalancesByOwenerId(ownerId)
            .orElseThrow(() -> new AccountBalanceNotFoundException(ownerId));

    if (Boolean.TRUE.equals(converted)) {
      ConversionDetails conversionDetails = convertAccountBalanceToPerferedCurrency(accountBalance);
      return AccountBalanceDto.fromEntity(accountBalance, conversionDetails);
    } else {
      return AccountBalanceDto.fromEntity(accountBalance);
    }
  }

  @Override
  public AccountBalanceDto getAccountBalance(UUID accountId, Boolean converted) {
    AccountBalance accountBalance =
        accountBalanceRepository
            .findAccountBalanceById(accountId)
            .orElseThrow(() -> new AccountBalanceNotFoundException(accountId));

    if (Boolean.TRUE.equals(converted)) {
      ConversionDetails conversionDetails = convertAccountBalanceToPerferedCurrency(accountBalance);
      return AccountBalanceDto.fromEntity(accountBalance, conversionDetails);
    } else {
      return AccountBalanceDto.fromEntity(accountBalance);
    }
  }

  private ConversionDetails convertAccountBalanceToPerferedCurrency(AccountBalance accountBalance) {
    return exchangeService.exchange(
        accountBalance.getSystemCurrency(),
        accountBalance.getPreferredCurrency(),
        accountBalance.getBalance());
  }

  @Override
  public Page<AccountBalanceDto> findAccountBalances(
      int size,
      int page,
      String sort,
      String direction,
      String ownerId,
      Boolean verified,
      String accountType,
      String accountId,
      Boolean converted,
      String stripeAccountId) {

    UUID accountIdUuid = null;
    UUID ownerIdUuid = null;
    Sort.Direction sortDirection = Sort.Direction.ASC;

    try {
      accountIdUuid = UUID.fromString(accountId);
      ownerIdUuid = UUID.fromString(ownerId);
      sortDirection = Sort.Direction.fromString(direction);
    } catch (IllegalArgumentException e) {
      log.warn("Account id {} is not UUID", accountId);
    }

    Specification<AccountBalance> specification =
        AccountBalanceSpecifications.build(
            verified, accountIdUuid, stripeAccountId, ownerIdUuid, accountType);

    Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
    Page<AccountBalance> accountBalances =
        accountBalanceRepository.findAll(specification, pageable);

    if (Boolean.TRUE.equals(converted)) {
      return accountBalances.map(
          accountBalance ->
              AccountBalanceDto.fromEntity(
                  accountBalance, convertAccountBalanceToPerferedCurrency(accountBalance)));
    } else {
      return accountBalances.map(AccountBalanceDto::fromEntity);
    }
  }

  @Override
  @Retryable(retryFor = AccountBalanceVerificationException.class)
  public AccountBalanceDto verifyUserAccount(UUID ownerId, KYC kyc)
      throws AccountBalanceVerificationException, AccountBalanceAlreadyVerifiedException {
    AccountBalance accountBalance =
        accountBalanceRepository
            .findAccountBalanceById(ownerId)
            .orElseThrow(() -> new AccountBalanceNotFoundException(ownerId));

    if (Boolean.TRUE.equals(accountBalance.getIsVerifiedAccount())) {
      throw new AccountBalanceAlreadyVerifiedException(accountBalance.getId());
    }

    if (accountBalance.getStripeAccountId() == null) {
      createStripeAccount(accountBalance.getOwnerId(), kyc.address().countryCode(), kyc.email());
      throw new AccountBalanceVerificationException(
          "Account Balance verification: Stripe account not created yet");
    }

    try {
      stripeIntegrationService.verifyConnectedAccount(accountBalance.getStripeAccountId(), kyc);
      accountBalance.verifyAccount();
      accountBalanceRepository.save(accountBalance);
      return AccountBalanceDto.fromEntity(accountBalance);
    } catch (StripeException e) {
      e.printStackTrace();
      throw new AccountBalanceVerificationException(
          "Account Balance verification:" + e.getMessage());
    }
  }
}
