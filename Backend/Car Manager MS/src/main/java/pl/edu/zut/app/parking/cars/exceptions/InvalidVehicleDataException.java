package pl.edu.zut.app.parking.cars.exceptions;

public class InvalidVehicleDataException extends VehicleServiceException {
    public InvalidVehicleDataException(String message) {
        super(message);
    }
}
