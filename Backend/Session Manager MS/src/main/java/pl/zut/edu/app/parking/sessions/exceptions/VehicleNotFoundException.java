package pl.zut.edu.app.parking.sessions.exceptions;

public class VehicleNotFoundException extends VehicleServiceException {

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
