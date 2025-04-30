package pl.edu.zut.app.parking.owners.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.requests.PersonalDataRegistrationRequest;
import pl.edu.zut.app.parking.owners.entities.Address;
import pl.edu.zut.app.parking.owners.entities.Owner;
import pl.edu.zut.app.parking.owners.exceptions.ParkingOwnerAlreadyExistsException;
import pl.edu.zut.app.parking.owners.services.AddressRepositoryService;
import pl.edu.zut.app.parking.owners.services.OwnerRepositoryService;
import pl.edu.zut.app.parking.owners.services.ParkingOwnerRegistrationService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PORegistrationServiceImpl implements ParkingOwnerRegistrationService {

    private final OwnerRepositoryService ownerRepositoryService;
    private final AddressRepositoryService addressRepositoryService;

    @Override
    public void createParkingOwner(UUID ownerId) {
        if (ownerRepositoryService.existsByOwnerId(ownerId)) {
            log.warn("Owner with id {} already exists", ownerId);
            throw new ParkingOwnerAlreadyExistsException(ownerId);
        }
        Owner owner = Owner.builder()
                .userId(ownerId)
                .isDeleted(false)
                .registrationCompleted(false)
                .build();
        try {
            ownerRepositoryService.save(owner);
        } catch (Exception e) {
            log.error("Error while creating owner with id {}", ownerId, e);
            throw e;
        }
    }

    @Override
    public void fillOwnerData(PersonalDataRegistrationRequest request) {
        UUID ownerId = SecurityContextUtil.extractUserIdFromSecurityContext();
        Owner owner = ownerRepositoryService.findOwnerById(ownerId);

        validateOwnerNotRegistered(owner);
        validateOwnerHasNoName(owner);

        owner.setFirstName(request.firstName());
        owner.setMiddleName(request.middleName());
        owner.setLastName(request.lastName());
        owner.setNip(request.NIP());
        owner.setPhoneNumber(request.phoneNumber());
        owner.setPhoneNumberCode(request.phoneNumberCode());

        ownerRepositoryService.save(owner);
    }

    @Override
    public void fillOwnerAddress(AddressDto addressDto) {
        UUID ownerId = SecurityContextUtil.extractUserIdFromSecurityContext();
        Owner owner = ownerRepositoryService.findOwnerById(ownerId);

        validateOwnerNotRegistered(owner);

        if (owner.getAddress() != null) {
            log.warn("Owner with id {} already has address", owner.getId());
            return;
        }

        Address address = Address.builder()
                .country(addressDto.country())
                .city(addressDto.city())
                .street(addressDto.street())
                .postalCode(addressDto.postalCode())
                .apartmentNumber(addressDto.apartmentNumber())
                .houseNumber(addressDto.houseNumber())
                .owner(owner)
                .build();

        owner.setAddress(address);
        Address savedAddress = addressRepositoryService.save(address);
        owner.setAddress(savedAddress);
        ownerRepositoryService.save(owner);
        this.markRegistrationComplete(owner.getUserId());
    }

    private void validateOwnerNotRegistered(Owner owner) {
        if (owner.isRegistrationCompleted()) {
            log.warn("Owner with id {} already completed registration", owner.getId());
            throw new IllegalStateException("Owner registration is already completed");
        }
    }

    private void validateOwnerHasNoName(Owner owner) {
        if ((owner.getFirstName() != null && !owner.getFirstName().isEmpty()) ||
                (owner.getMiddleName() != null && !owner.getMiddleName().isEmpty()) ||
                (owner.getLastName() != null && !owner.getLastName().isEmpty())) {

            log.warn("Owner with id {} already has name details", owner.getId());
            throw new IllegalStateException("Owner already has name details");
        }
    }

    public void markRegistrationComplete(UUID ownerId) {
        Owner owner = ownerRepositoryService.findOwnerById(ownerId);
        validateOwnerNotRegistered(owner);

        owner.setRegistrationCompleted(true);
        ownerRepositoryService.save(owner);
    }
}
