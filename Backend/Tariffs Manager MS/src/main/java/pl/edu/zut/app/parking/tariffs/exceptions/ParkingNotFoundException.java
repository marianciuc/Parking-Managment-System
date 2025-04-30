package pl.edu.zut.app.parking.tariffs.exceptions;

public class ParkingNotFoundException extends TariffServiceException {
    public ParkingNotFoundException(String s) {
        super(s);
    }
}
