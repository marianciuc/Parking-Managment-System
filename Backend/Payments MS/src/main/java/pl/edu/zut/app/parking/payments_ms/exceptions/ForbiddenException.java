package pl.edu.zut.app.parking.payments_ms.exceptions;

public class ForbiddenException extends PaymentServiceException {
    public ForbiddenException(String message) {
        super(message);
    }
}
