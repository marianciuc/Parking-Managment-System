package pl.edu.zut.app.parking.payments_ms.exceptions;

public class AccountBalanceVerificationException extends PaymentServiceException {
  public AccountBalanceVerificationException(String message) {
    super(message);
  }
}
