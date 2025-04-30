package pl.edu.zut.app.parking.payments_ms.exceptions;

public class AccountCreationException extends PaymentServiceException {
    public AccountCreationException(String message) {
        super(message);
    }
}
