package pl.edu.zut.app.parking.cars.exceptions;

public class VehicleAlreadyExistsException extends VehicleServiceException {
    public VehicleAlreadyExistsException(String message) {
        super(message);
    }
}
