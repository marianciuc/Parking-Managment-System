package pl.edu.zut.app.parking.payments_ms.exceptions;

public class TransactionNotFoundException extends PaymentServiceException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
