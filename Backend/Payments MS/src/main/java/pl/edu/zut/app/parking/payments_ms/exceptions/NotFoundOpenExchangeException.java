package pl.edu.zut.app.parking.payments_ms.exceptions;

public class NotFoundOpenExchangeException extends ExchangeException {
    public NotFoundOpenExchangeException() {
        super("Client requested a non-existent resource/route" + " or the resource is not available in the requested currency");
    }
}
