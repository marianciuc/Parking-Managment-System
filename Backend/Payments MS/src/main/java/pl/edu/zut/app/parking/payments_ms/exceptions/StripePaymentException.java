package pl.edu.zut.app.parking.payments_ms.exceptions;

public class StripePaymentException extends PaymentServiceException {
  public StripePaymentException(String message) {
    super(message);
  }
  public StripePaymentException(String message, Throwable cause) {
    super(message);
    initCause(cause);
  }
}
