package pl.edu.zut.app.parking.payments_ms.exceptions;

public class InvalidBaseOpenExchangeException extends ExchangeException {
    public InvalidBaseOpenExchangeException(String currency) {
        super("Invalid base currency: " + currency);
    }
}
