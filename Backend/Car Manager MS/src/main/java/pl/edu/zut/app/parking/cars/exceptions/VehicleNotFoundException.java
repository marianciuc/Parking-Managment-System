package pl.edu.zut.app.parking.cars.exceptions;

public class VehicleNotFoundException extends VehicleServiceException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
