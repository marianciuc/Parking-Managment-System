package pl.edu.zut.app.parking.payments_ms.services;

import java.util.UUID;
import pl.edu.zut.app.parking.payments_ms.dto.PaymentRequest;
import pl.edu.zut.app.parking.payments_ms.dto.WithdrawRequest;
import pl.edu.zut.app.parking.payments_ms.dto.common.TransactionDto;
import pl.edu.zut.app.parking.payments_ms.exceptions.DepositProcessExceptiom;
import pl.edu.zut.app.parking.payments_ms.exceptions.WithdrawProcessException;

/** Payment service interface to handle deposit, withdrawal, and other payment operations. */
public interface PaymentService {

  /**
   * Processes a withdrawal request by transferring the specified amount from the user's account to
   * the designated bank account.
   *
   * @param withdrawRequest the request containing details such as the bank account ID and the
   *     amount to withdraw
   * @param userId the unique identifier of the user initiating the withdrawal
   * @return a {@link TransactionDto} representing the details of the completed transaction
   * @throws WithdrawProcessException if the withdrawal process fails due to insufficient balance,
   *     invalid bank account, or other processing issues
   */
  TransactionDto withdraw(WithdrawRequest withdrawRequest, UUID userId)
      throws WithdrawProcessException;

  /**
   * Adds a specified amount to the account balance associated with the given account ID.
   *
   * @param paymentRequest contains the details of the top-up operation, including the amount and
   *     currency
   * @param accountId the unique identifier of the account to which the top-up is applied
   * @return a String confirmation or reference of the successful top-up operation
   * @throws DepositProcessExceptiom if the top-up process fails due to validation issues, system
   *     errors, or other reasons
   */
  String deposit(PaymentRequest paymentRequest, UUID accountId) throws DepositProcessExceptiom;
}
