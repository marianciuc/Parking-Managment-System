package pl.edu.zut.app.parking.payments_ms.exceptions;

public class BalanceAccountInvalidOperationException extends PaymentServiceException {
    public BalanceAccountInvalidOperationException(String message) {
        super(message);
    }
}
