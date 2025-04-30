package pl.edu.zut.app.parking.payments_ms.exceptions;

public class TransactionCreationException extends PaymentServiceException {
  public TransactionCreationException(String message) {
    super(message);
  }
  public TransactionCreationException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
