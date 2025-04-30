package pl.edu.zut.app.parking.parking.exceptions;

import java.util.UUID;

public class AddressNotFoundException extends ParkingException {
    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(UUID parkingId) {
        super("Address not found for parking with id: " + parkingId);
    }
}
