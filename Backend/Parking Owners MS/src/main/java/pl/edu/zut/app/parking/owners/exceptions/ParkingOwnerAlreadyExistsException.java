package pl.edu.zut.app.parking.owners.exceptions;

import java.util.UUID;

public class ParkingOwnerAlreadyExistsException extends RuntimeException {
    public ParkingOwnerAlreadyExistsException(UUID ownerId) {
        super("Parking owner with id " + ownerId + " already exists");
    }
}
