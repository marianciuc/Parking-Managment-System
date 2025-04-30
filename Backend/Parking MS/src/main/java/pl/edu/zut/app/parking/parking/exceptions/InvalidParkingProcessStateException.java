package pl.edu.zut.app.parking.parking.exceptions;

public class InvalidParkingProcessStateException extends RuntimeException {
    public InvalidParkingProcessStateException(String message) {
        super(message);
    }
}
