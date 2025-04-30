package pl.edu.zut.app.parking.payments_ms.exceptions;

public class MissingAppIdOpenExchangeException extends ExchangeException {
    public MissingAppIdOpenExchangeException() {
        super("Client did not provide an App ID");
    }
}
