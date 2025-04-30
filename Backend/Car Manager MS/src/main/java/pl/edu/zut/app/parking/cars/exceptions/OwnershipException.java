package pl.edu.zut.app.parking.cars.exceptions;

public abstract class OwnershipException extends VehicleServiceException {
    public OwnershipException(String message) {
        super(message);
    }
}
