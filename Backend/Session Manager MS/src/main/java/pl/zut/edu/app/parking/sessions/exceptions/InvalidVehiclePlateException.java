package pl.zut.edu.app.parking.sessions.exceptions;

public class InvalidVehiclePlateException extends SessionServiceException {
    public InvalidVehiclePlateException(String plate) {
        super("Invalid vehicle plate: " + plate);
    }
}
