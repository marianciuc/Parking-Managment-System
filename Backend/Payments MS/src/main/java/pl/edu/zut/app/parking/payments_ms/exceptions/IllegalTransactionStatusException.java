package pl.edu.zut.app.parking.payments_ms.exceptions;

public class IllegalTransactionStatusException extends PaymentServiceException {
    public IllegalTransactionStatusException(String message) {
        super(message);
    }
}
