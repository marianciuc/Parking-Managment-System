package pl.edu.zut.app.parking.payments_ms.exceptions;

import java.util.UUID;

public class AccountBalanceNotFoundException extends PaymentServiceException {
    public AccountBalanceNotFoundException(String message) {
        super(message);
    }

    public AccountBalanceNotFoundException(UUID id) {
        super("Account balance not found" + id);
    }
}
