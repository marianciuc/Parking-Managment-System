package pl.edu.zut.app.parking.tariffs.exceptions;

public abstract class TariffServiceException extends RuntimeException {
    public TariffServiceException(String message) {
        super(message);
    }
}
