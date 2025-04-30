package pl.edu.zut.app.parking.payments_ms.services.impl;

import com.stripe.exception.StripeException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.payments_ms.dto.PaymentRequest;
import pl.edu.zut.app.parking.payments_ms.dto.WithdrawRequest;
import pl.edu.zut.app.parking.payments_ms.dto.common.AccountBalanceDto;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.enums.AccountType;
import pl.edu.zut.app.parking.payments_ms.exceptions.DepositProcessExceptiom;
import pl.edu.zut.app.parking.payments_ms.exceptions.InsufficientFundsException;
import pl.edu.zut.app.parking.payments_ms.exceptions.WithdrawProcessException;
import pl.edu.zut.app.parking.payments_ms.integrations.StripeIntegrationService;
import pl.edu.zut.app.parking.payments_ms.services.AccountBalanceService;
import pl.edu.zut.app.parking.payments_ms.services.PaymentService;
import pl.edu.zut.app.parking.payments_ms.services.TransactionService;

@Service
@Slf4j
@RequiredArgsConstructor
public class StripePaymentServiceImpl implements PaymentService {

  private final TransactionService transactionsService;
  private final AccountBalanceService accountBalanceService;
  private final StripeIntegrationService stripeIntegrationService;

  @Override
  @Transactional
  public TransactionDto withdraw(WithdrawRequest withdrawRequest, UUID userId)
      throws WithdrawProcessException {
    log.info("Starting withdrawal for user {}", userId);
    AccountBalanceDto accountBalance = accountBalanceService.getOwnerAccountBalance(userId, false);

    if (Boolean.FALSE.equals(accountBalance.isVerifiedAccount())) {
      log.error("User account is not verified to withdraw money");
      throw new WithdrawProcessException("User account is not verified to withdraw money");
    }

    if (!accountBalance.accountType().equals(AccountType.BUSINESS)) {
      log.error(
          "This operation is not supported for this account type: {}",
          accountBalance.accountType());
      throw new WithdrawProcessException("This operation is not supported for this account type");
    }

    if (accountBalance.stripeAccountId() == null) {
      log.error("Stripe account id is not set for account {}", accountBalance.id());
      throw new IllegalStateException("Stripe account id is not set");
    }

    try {
      TransactionDto transactionDto =
          transactionsService.createWithdrawTransaction(
              withdrawRequest.amount(),
              accountBalance.preferredCurrency(),
              accountBalance.id(),
              withdrawRequest.bankAccountId());

      stripeIntegrationService.createPayout(
          accountBalance.stripeAccountId(),
          withdrawRequest.amount(),
          accountBalance.preferredCurrency(),
          transactionDto.id());

      log.info(
          "Withdrawal successful for user {} with transaction ID: {}", userId, transactionDto.id());
      return transactionDto;
    } catch (StripeException e) {
      log.error("StripeException occurred: {}", e.getMessage(), e);
      throw new WithdrawProcessException(e.getMessage(), e);
    } catch (InsufficientFundsException e) {
      log.error("InsufficientFundsException occurred: {}", e.getMessage(), e);
      throw new WithdrawProcessException(e.getMessage(), e);
    }
  }

  @Override
  public String deposit(PaymentRequest paymentRequest, UUID accountId)
      throws DepositProcessExceptiom {
    log.info("Starting top-up for account {}", accountId);
    TransactionDto transactionDto =
        transactionsService.createTopUpBalanceTransaction(
            accountId, paymentRequest.amount(), paymentRequest.currency());

    try {
      String paymentIntentId =
          stripeIntegrationService.createPaymentIntent(
              paymentRequest.amount(), paymentRequest.currency(), transactionDto.id());

      log.info(
          "Top-up successful for account {} with transaction ID: {}",
          accountId,
          transactionDto.id());
      return paymentIntentId;

    } catch (StripeException e) {
      log.error("StripeException occurred: {}", e.getMessage(), e);
      throw new DepositProcessExceptiom(e.getMessage(), e);
    }
  }
}
