package pl.edu.zut.app.parking.payments_ms.exceptions;

public class DepositProcessExceptiom extends PaymentServiceException {
  public DepositProcessExceptiom(String message) {
    super(message);
  }
  public DepositProcessExceptiom(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
