package pl.edu.zut.app.parking.cars.exceptions;

public class InvalidRequestStatusException extends OwnershipException {
    public InvalidRequestStatusException(String message) {
        super(message);
    }
}
