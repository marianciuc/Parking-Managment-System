package pl.edu.zut.app.parking.payments_ms.exceptions;

public class NotAllowedOpenExchangeException extends ExchangeException {
    public NotAllowedOpenExchangeException() {
        super("Client doesn’t have permission to access requested route/feature");
    }
}
