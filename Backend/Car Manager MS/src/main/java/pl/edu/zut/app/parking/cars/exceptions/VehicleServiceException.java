package pl.edu.zut.app.parking.cars.exceptions;

public abstract class VehicleServiceException extends RuntimeException {
    public VehicleServiceException(String message) {
        super(message);
    }
}
