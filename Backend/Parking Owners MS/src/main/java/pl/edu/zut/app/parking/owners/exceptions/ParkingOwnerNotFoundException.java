package pl.edu.zut.app.parking.owners.exceptions;

import java.util.UUID;

public class ParkingOwnerNotFoundException extends RuntimeException {
    public ParkingOwnerNotFoundException(UUID ownerId) {
        super("Parking owner with id " + ownerId + " not found.");
    }
}
