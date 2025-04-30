package pl.edu.zut.app.parking.tariffs.exceptions;

public class TariffNotFoundException extends TariffServiceException {
    public TariffNotFoundException(String message) {
        super(message);
    }
}
