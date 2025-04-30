package pl.edu.zut.app.parking.owners.services;

import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.ParkingOwnerDto;

import java.util.UUID;

public interface OwnerService {
    ParkingOwnerDto updateOwnerAddress(UUID userId, AddressDto req);

    ParkingOwnerDto updateOwner(UUID userId, ParkingOwnerDto req);
}
