package pl.edu.zut.app.parking.payments_ms.exceptions;

public class WithdrawProcessException extends PaymentServiceException {
  public WithdrawProcessException(String message) {
    super(message);
  }
  public WithdrawProcessException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
