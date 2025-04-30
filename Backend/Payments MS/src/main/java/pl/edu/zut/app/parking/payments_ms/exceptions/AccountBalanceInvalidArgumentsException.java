package pl.edu.zut.app.parking.payments_ms.exceptions;

public class AccountBalanceInvalidArgumentsException extends PaymentServiceException {
    public AccountBalanceInvalidArgumentsException(String message) {
        super(message);
    }
}
