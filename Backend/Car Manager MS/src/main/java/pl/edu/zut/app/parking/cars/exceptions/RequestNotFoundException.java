package pl.edu.zut.app.parking.cars.exceptions;

public class RequestNotFoundException extends OwnershipException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
