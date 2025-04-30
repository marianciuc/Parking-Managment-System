package pl.edu.zut.app.parking.owners.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.ParkingOwnerDto;
import pl.edu.zut.app.parking.owners.entities.Address;
import pl.edu.zut.app.parking.owners.entities.Owner;
import pl.edu.zut.app.parking.owners.services.OwnerRepositoryService;
import pl.edu.zut.app.parking.owners.services.OwnerService;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnersServiceImpl implements OwnerService {

    private final OwnerRepositoryService ownerRepositoryService;


    @Override
    public ParkingOwnerDto updateOwnerAddress(UUID userId, AddressDto req) {
        Owner user = ownerRepositoryService.findOwnerById(userId);
        Address address = user.getAddress();
        address.setCity(req.city());
        address.setCountry(req.country());
        address.setHouseNumber(req.houseNumber());
        address.setApartmentNumber(req.apartmentNumber());
        address.setPostalCode(req.postalCode());
        address.setStreet(req.street());
        return ParkingOwnerDto.fromEntity(ownerRepositoryService.save(user));
    }

    @Override
    public ParkingOwnerDto updateOwner(UUID userId, ParkingOwnerDto req) {
        Owner user = ownerRepositoryService.findOwnerById(userId);
        user.setFirstName(req.firstName());
        user.setMiddleName(req.middleName());
        user.setLastName(req.lastName());
        user.setNip(req.NIP());
        user.setPhoneNumber(req.phoneNumber());
        user.setDateOfBirth(req.dateOfBirth());
        return ParkingOwnerDto.fromEntity(ownerRepositoryService.save(user));
    }
}
