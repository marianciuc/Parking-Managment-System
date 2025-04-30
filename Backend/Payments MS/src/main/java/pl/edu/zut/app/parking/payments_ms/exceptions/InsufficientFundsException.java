package pl.edu.zut.app.parking.payments_ms.exceptions;

public class InsufficientFundsException extends PaymentServiceException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}
