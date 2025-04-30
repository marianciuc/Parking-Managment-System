package pl.edu.zut.app.parking.parking.exceptions;

import java.util.UUID;

public class AddressAlreadyExistsException extends ParkingException {
    public AddressAlreadyExistsException(UUID id) {
        super("Address already exists for parking with id: " + id);
    }
}
