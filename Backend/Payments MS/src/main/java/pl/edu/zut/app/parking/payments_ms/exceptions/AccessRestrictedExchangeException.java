package pl.edu.zut.app.parking.payments_ms.exceptions;

public class AccessRestrictedExchangeException extends ExchangeException {
    public AccessRestrictedExchangeException(String message) {
        super(message);
    }
}
