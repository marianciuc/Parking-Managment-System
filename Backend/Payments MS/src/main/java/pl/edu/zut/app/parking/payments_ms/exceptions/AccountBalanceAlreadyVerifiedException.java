package pl.edu.zut.app.parking.payments_ms.exceptions;

import java.util.UUID;

public class AccountBalanceAlreadyVerifiedException extends PaymentServiceException {
  public AccountBalanceAlreadyVerifiedException(String message) {
    super(message);
  }

  public AccountBalanceAlreadyVerifiedException(UUID id) {
    super("Account balance already verified" + id);
  }
}
