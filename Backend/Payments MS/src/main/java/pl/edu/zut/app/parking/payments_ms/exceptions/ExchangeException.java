package pl.edu.zut.app.parking.payments_ms.exceptions;

public abstract class ExchangeException extends PaymentServiceException {
    public ExchangeException(String message) {
        super(message);
    }
}
