package pl.edu.zut.app.parking.cars.services;

import pl.edu.zut.app.parking.cars.dto.OwnershipTransferRequest;
import pl.edu.zut.app.parking.cars.entities.Ownership;

import java.util.UUID;

public interface OwnershipService {

    Boolean requestOwnershipTransfer(UUID carId);

    Boolean transferOwnership(UUID carId, UUID newOwnerId, Ownership.OwnerType ownerType);

    void deleteOwnership(UUID carId);

    Boolean acceptRequest(UUID requestId);

    Boolean rejectRequest(UUID requestId);
}
