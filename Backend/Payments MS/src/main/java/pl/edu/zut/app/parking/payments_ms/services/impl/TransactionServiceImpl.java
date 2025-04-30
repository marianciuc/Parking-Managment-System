package pl.edu.zut.app.parking.payments_ms.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.entities.*;
import pl.edu.zut.app.parking.payments_ms.enums.Currency;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionStatus;
import pl.edu.zut.app.parking.payments_ms.enums.TransactionType;
import pl.edu.zut.app.parking.payments_ms.exceptions.IllegalTransactionStatusException;
import pl.edu.zut.app.parking.payments_ms.exceptions.TransactionCreationException;
import pl.edu.zut.app.parking.payments_ms.exceptions.TransactionNotFoundException;
import pl.edu.zut.app.parking.payments_ms.kafka.SessionPaymentMessageProducer;
import pl.edu.zut.app.parking.payments_ms.repositories.TransactionsRepository;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.ExchangeService;
import pl.edu.zut.app.parking.payments_ms.services.TransactionService;
import pl.edu.zut.app.parking.payments_ms.specifications.TransactionSpecifications;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final SessionPaymentMessageProducer sessionPaymentMessageProducer;
  @PersistenceContext private EntityManager entityManager;

  private final TransactionsRepository transactionsRepository;
  private final AccountBalanceService accountBalanceService;
  private final ExchangeService exchangeService;

  private TransactionDto transferBetweenAccounts(
      UUID fromAccountId,
      UUID toAccountId,
      BigDecimal amount,
      Currency currency,
      TransactionType type) {
    log.info(
        "Transfer between accounts: from {} to {} with amount {} and currency {}",
        fromAccountId,
        toAccountId,
        amount,
        currency);

    AccountBalance sourceAccount = entityManager.getReference(AccountBalance.class, fromAccountId);
    AccountBalance destinationAccount =
        entityManager.getReference(AccountBalance.class, toAccountId);

    if (type.equals(TransactionType.EXTERNAL_DEPOSIT)) {
      throw new IllegalArgumentException("This method cannot be used for deposit");
    }

    if (type.equals(TransactionType.WITHDRAWAL)) {
      throw new IllegalArgumentException("This method cannot be used for withdrawal");
    }

    if (sourceAccount.getAccountType().equals(destinationAccount.getAccountType())) {
      log.warn("Cannot transfer between accounts of the same type");
      throw new IllegalArgumentException("Cannot transfer between accounts of the same type");
    }

    ConversionDetails conversionDetails = exchangeService.exchange(currency, Currency.USD, amount);

    Transaction transaction =
        Transaction.builder()
            .sourceAccount(sourceAccount)
            .destinationAccount(destinationAccount)
            .conversionDetails(conversionDetails)
            .type(type)
            .metadata(new TransactionMetadata())
            .bankAccount(null)
            .status(TransactionStatus.PENDING)
            .build();

    TransactionDto transactionDto = TransactionDto.fromEntity(transaction);
    try {
      accountBalanceService.updateAccountBalance(transactionDto);
      return TransactionDto.fromEntity(successTransaction(transaction));
    } catch (Exception e) {
      log.error("Error during internal transfer", e);
      return TransactionDto.fromEntity(failureTransaction(transaction, e.getMessage()));
    }
  }

  private Transaction failureTransaction(Transaction transaction, String failureMessage) {
    if (transaction.getStatus() != TransactionStatus.PENDING) {
      throw new IllegalTransactionStatusException("Only pending transactions can be failed.");
    }
    transaction.setStatus(TransactionStatus.FAILED);
    transaction.setFailureMessage(failureMessage);
    log.info("Transaction failed with message {}", failureMessage);
    return transactionsRepository.save(transaction);
  }

  private Transaction successTransaction(Transaction transaction) {
    if (transaction.getStatus() != TransactionStatus.PENDING) {
      throw new IllegalTransactionStatusException("Only pending transactions can be completed.");
    }
    transaction.setStatus(TransactionStatus.COMPLETED);
    log.info("Transaction completed successfully");
    return transactionsRepository.save(transaction);
  }

  private Transaction cancelTransaction(Transaction transaction) {
    if (transaction.getStatus() != TransactionStatus.PENDING) {
      throw new IllegalTransactionStatusException("Only pending transactions can be completed.");
    }
    transaction.setStatus(TransactionStatus.CANCELLED);
    return transactionsRepository.save(transaction);
  }

  @Override
  public TransactionDto getTransaction(UUID transactionId) {
    return TransactionDto.fromEntity(
        transactionsRepository
            .findById(transactionId)
            .orElseThrow(() -> new TransactionNotFoundException("")));
  }

  @Override
  public TransactionDto createTopUpBalanceTransaction(
      UUID accountId, BigDecimal amount, Currency currency) {
    ConversionDetails conversionDetails = exchangeService.exchange(currency, Currency.USD, amount);
    AccountBalance destinationAccoount =
        entityManager.getReference(AccountBalance.class, accountId);

    Transaction transaction =
        Transaction.builder()
            .status(TransactionStatus.PENDING)
            .type(TransactionType.EXTERNAL_DEPOSIT)
            .conversionDetails(conversionDetails)
            .destinationAccount(destinationAccoount)
            .metadata(new TransactionMetadata())
            .bankAccount(null)
            .sourceAccount(null)
            .build();

    return TransactionDto.fromEntity(transactionsRepository.save(transaction));
  }

  public TransactionDto createSessionPaymentTransaction(
      TransactionType type,
      UUID sessionId,
      BigDecimal amount,
      Currency currency,
      UUID parkingId,
      UUID parkingBuisnessAccountId,
      UUID carOwenrAccountId)
      throws TransactionCreationException {
    ConversionDetails conversionDetails = exchangeService.exchange(currency, Currency.USD, amount);
    AccountBalance parkingAccountBalance =
        entityManager.getReference(AccountBalance.class, parkingBuisnessAccountId);
    TransactionMetadata metadata =
        TransactionMetadata.builder().sessionId(sessionId).parkingId(parkingId).build();

    if (type.equals(TransactionType.SESSION_EXTERNAL_PAYMENT)) {

      Transaction transaction =
          Transaction.builder()
              .status(TransactionStatus.PENDING)
              .type(type)
              .conversionDetails(conversionDetails)
              .destinationAccount(parkingAccountBalance)
              .metadata(metadata)
              .build();

      return TransactionDto.fromEntity(transactionsRepository.save(transaction));
    } else if (type.equals(TransactionType.SESSIONS_PAYMENT)) {

      if (carOwenrAccountId == null) {
        throw new TransactionCreationException("Car owner account id cannot be null");
      }

      AccountBalance ownerAccountBalance =
          entityManager.getReference(AccountBalance.class, carOwenrAccountId);

      Transaction transaction =
          Transaction.builder()
              .status(TransactionStatus.PENDING)
              .type(type)
              .conversionDetails(conversionDetails)
              .destinationAccount(parkingAccountBalance)
              .metadata(metadata)
              .sourceAccount(ownerAccountBalance)
              .build();

      TransactionDto transactionDto =
          TransactionDto.fromEntity(transactionsRepository.save(transaction));

      try {
        accountBalanceService.updateAccountBalance(transactionDto);
        return TransactionDto.fromEntity(successTransaction(transaction));
      } catch (Exception e) {
        log.error("Error during internal transfer", e);
        return TransactionDto.fromEntity(failureTransaction(transaction, e.getMessage()));
      }
    }
    throw new TransactionCreationException("Unsupported transaction type " + type);
  }

  @Override
  public Page<TransactionDto> findTransactions(
      Integer page,
      Integer size,
      String sort,
      String direction,
      UUID transactionId,
      UUID bankAccountId,
      TransactionStatus status,
      UUID sourceAccountId,
      UUID destinationAccountId,
      UUID parkingId,
      UUID sessionId,
      String stripePaymentId,
      Boolean converted,
      Currency currency,
      TransactionType transactionType,
      String subscriptionOrderId) {
    Sort.Direction sortDirection = Sort.Direction.ASC;

    try {
      Sort.Direction.fromString(direction);
    } catch (IllegalArgumentException e) {
      log.warn("Invalid sort direction {}", direction);
    }
    Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
    Specification<Transaction> specification =
        TransactionSpecifications.build(
            transactionId,
            bankAccountId,
            status,
            sourceAccountId,
            destinationAccountId,
            parkingId,
            sessionId,
            stripePaymentId,
            transactionType,
            subscriptionOrderId);

    Page<Transaction> transactionsPage = transactionsRepository.findAll(specification, pageable);
    if (converted != null && converted) {
      return transactionsPage.map(
          transaction -> {
            ConversionDetails conversionDetails =
                exchangeService.exchange(
                    transaction.getConversionDetails().getDestinationCurrency(),
                    currency,
                    transaction.getConversionDetails().getDestinationAmount());
            return TransactionDto.fromEntity(transaction, conversionDetails);
          });
    }
    return transactionsPage.map(TransactionDto::fromEntity);
  }

  @Override
  public TransactionDto createWithdrawTransaction(
      BigDecimal amount, Currency currency, UUID accountBalanceId, UUID bankAccountId) {
    AccountBalance accountBalance =
        entityManager.getReference(AccountBalance.class, accountBalanceId);
    BankAccount bankAccount = entityManager.getReference(BankAccount.class, bankAccountId);
    ConversionDetails conversionDetails = exchangeService.exchange(currency, Currency.USD, amount);

    Transaction transaction =
        Transaction.builder()
            .status(TransactionStatus.PENDING)
            .type(TransactionType.WITHDRAWAL)
            .conversionDetails(conversionDetails)
            .sourceAccount(accountBalance)
            .bankAccount(bankAccount)
            .metadata(new TransactionMetadata())
            .destinationAccount(null)
            .build();

    return TransactionDto.fromEntity(transactionsRepository.save(transaction));
  }

  @Override
  public void updateTransactionStatus(
      UUID transactionId, boolean isSuccess, String failureMessage) {
    log.info(">(updateTransactionStatus) Transaction with id {} is {}", transactionId, isSuccess);
    Transaction transaction =
        transactionsRepository
            .findById(transactionId)
            .orElseThrow(
                () ->
                    new TransactionNotFoundException(
                        "Transaction with id " + transactionId + " not found"));

    log.info("Found Transaction: {}", transaction);

    TransactionDto transactionWithDecision =
        isSuccess
            ? TransactionDto.fromEntity(successTransaction(transaction))
            : TransactionDto.fromEntity(failureTransaction(transaction, failureMessage));

    log.info("Decision: {}", transactionWithDecision.status());

    if ((transaction.getType().equals(TransactionType.SESSIONS_PAYMENT)
            || transaction.getType().equals(TransactionType.SESSION_EXTERNAL_PAYMENT))
        && transactionWithDecision.status().equals(TransactionStatus.COMPLETED)) {
      log.info("Sending message to session payment service for transaction {}", transactionId);
      try{
        sessionPaymentMessageProducer.sendMessage(
            transactionWithDecision.metadata().sessionId(),
            transactionWithDecision.conversionDetails().destinationAmount(),
            transactionWithDecision.conversionDetails().destinationCurrency());
      } catch (Exception e) {
        log.error("Error sending message to session payment service for transaction {}", transactionId, e);
      }
    }

    accountBalanceService.updateAccountBalance(transactionWithDecision);
  }
}
