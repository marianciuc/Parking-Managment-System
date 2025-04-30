package pl.edu.zut.app.parking.parking.exceptions;

public abstract class ParkingException extends RuntimeException {
    public ParkingException(String message) {
        super(message);
    }
}
