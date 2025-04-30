package pl.edu.zut.app.parking.cars.exceptions;

public abstract class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }
}
