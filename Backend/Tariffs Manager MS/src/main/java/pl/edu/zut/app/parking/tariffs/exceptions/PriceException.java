package pl.edu.zut.app.parking.tariffs.exceptions;

public abstract class PriceException extends RuntimeException {
    public PriceException(String message) {
        super(message);
    }
}
