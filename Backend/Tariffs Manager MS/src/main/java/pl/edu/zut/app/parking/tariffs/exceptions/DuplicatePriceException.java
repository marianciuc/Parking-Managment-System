package pl.edu.zut.app.parking.tariffs.exceptions;

public class DuplicatePriceException extends PriceException {
    public DuplicatePriceException(String message) {
        super(message);
    }
}
