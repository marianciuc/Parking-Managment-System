package pl.edu.zut.app.parking.payments_ms.exceptions;

public class SessionPaymentException extends PaymentServiceException {
  public SessionPaymentException(String message) {
    super(message);
  }
  public SessionPaymentException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
