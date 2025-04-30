package pl.edu.zut.app.parking.payments_ms.exceptions;

public class InvalidAppIdOpenExchangeException extends ExchangeException {
    public InvalidAppIdOpenExchangeException() {
        super("Client provided an invalid App ID");
    }
}
