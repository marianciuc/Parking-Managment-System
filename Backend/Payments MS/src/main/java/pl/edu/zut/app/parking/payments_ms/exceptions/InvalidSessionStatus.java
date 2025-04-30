package pl.edu.zut.app.parking.payments_ms.exceptions;

public class InvalidSessionStatus extends PaymentServiceException {
    public InvalidSessionStatus(String message) {
        super(message);
    }
}
