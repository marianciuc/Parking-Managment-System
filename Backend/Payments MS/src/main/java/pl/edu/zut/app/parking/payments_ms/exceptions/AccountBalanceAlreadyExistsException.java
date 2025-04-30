package pl.edu.zut.app.parking.payments_ms.exceptions;

public class AccountBalanceAlreadyExistsException extends PaymentServiceException {
    public AccountBalanceAlreadyExistsException(String message) {
        super(message);
    }
}
