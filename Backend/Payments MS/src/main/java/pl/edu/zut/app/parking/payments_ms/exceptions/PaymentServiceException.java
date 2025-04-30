package pl.edu.zut.app.parking.payments_ms.exceptions;

public abstract class PaymentServiceException extends RuntimeException {
    public PaymentServiceException(String message) {
        super(message);
    }
}
