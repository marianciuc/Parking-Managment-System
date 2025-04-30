package pl.edu.zut.app.parking.payments_ms.exceptions;

public class BankAccountCreationException extends PaymentServiceException {
  public BankAccountCreationException(String message) {
    super(message);
  }
  public BankAccountCreationException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
