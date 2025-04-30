package pl.edu.zut.app.parking.payments_ms.exceptions;

public class BankAccountNotFoundException extends PaymentServiceException {
  public BankAccountNotFoundException(String message) {
    super(message);
  }
  public BankAccountNotFoundException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
