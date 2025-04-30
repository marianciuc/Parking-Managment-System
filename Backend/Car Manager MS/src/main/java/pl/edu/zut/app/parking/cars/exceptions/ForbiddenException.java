package pl.edu.zut.app.parking.cars.exceptions;

public class ForbiddenException extends SecurityException {

    public ForbiddenException(String message) {
        super(message);
    }
}
