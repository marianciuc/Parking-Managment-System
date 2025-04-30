package pl.zut.edu.app.parking.sessions.exceptions;

public abstract class VehicleServiceException extends RuntimeException {
    public VehicleServiceException(String message) {
        super(message);
    }
}
