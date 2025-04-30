package pl.edu.zut.app.parking.tariffs.exceptions;

public class TariffAlreadyExistsException extends TariffServiceException {
    public TariffAlreadyExistsException(String s) {
        super(s);
    }
}
