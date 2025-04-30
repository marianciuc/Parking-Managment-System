package pl.edu.zut.app.parking.parking.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Address;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.AddressAlreadyExistsException;
import pl.edu.zut.app.parking.parking.exceptions.AddressNotFoundException;
import pl.edu.zut.app.parking.parking.exceptions.InvalidOperationException;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;
import pl.edu.zut.app.parking.parking.repositories.AddressRepository;
import pl.edu.zut.app.parking.parking.services.AddressService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressDto find(UUID parkingId) {
        log.info("Finding address for parking: {}", parkingId);
        return addressRepository.findByParkingId(parkingId).map(AddressDto::fromEntity)
                .orElseThrow(() -> new AddressNotFoundException(parkingId));
    }

    @Override
    @Transactional
    public AddressDto update(UUID parkingId, AddressDto addressDto) {
        log.info("Updating address for parking: {}", parkingId);
        Address address = addressRepository.findByParkingId(parkingId).orElseThrow(() -> new AddressNotFoundException(parkingId));

        if (address.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED) {
            throw new NotFoundException("Parking address not found");
        }

        if (addressDto.isBelongToAnyInstitution()) {
            address.setBelongToAnyInstitution(true);
            address.setInstitutionName(addressDto.institutionName());
        } else {
            address.setBelongToAnyInstitution(false);
            address.setInstitutionName(null);
        }
        if (addressDto.countryCode() != null) {
            address.setCountryCode(addressDto.countryCode());
        }
        if (addressDto.city() != null) {
            address.setCity(addressDto.city());
        }
        if (addressDto.street() != null) {
            address.setStreet(addressDto.street());
        }
        if (addressDto.buildingNumber() != null) {
            address.setBuildingNumber(addressDto.buildingNumber());
        }
        if (addressDto.postalCode() != null) {
            address.setPostalCode(addressDto.postalCode());
        }
        return AddressDto.fromEntity(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void delete(UUID parkingId) {
        log.info("Deleting address for parking: {}", parkingId);
        Address address =
                addressRepository.findByParkingId(parkingId).orElseThrow(() -> new AddressNotFoundException(parkingId));

        if (address.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED) {
            log.info("Address for parking: {} already deleted", parkingId);
            throw new InvalidOperationException("Address for parking: " + parkingId + " already deleted");
        }

        address.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
        addressRepository.save(address);
        log.info("Address for parking: {} deleted", parkingId);
    }

    @Override
    public void deletePermanent(UUID id) throws AddressNotFoundException {
        log.info("Deleting address permanently for id: {}", id);
        Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressRepository.delete(address);
        log.info("Address permanently deleted for id: {}", id);
    }

    @Override
    public AddressDto create(Parking parking, AddressDto addressDto) {
        log.info("Creating new address for parking: {}", parking.getId());

        if (addressRepository.findByParkingId(parking.getId()).isPresent()) {
            log.info("Address for parking: {} already exists", parking.getId());
            throw new AddressAlreadyExistsException(parking.getId());
        }

        Address address = Address.builder()
                .institutionName(addressDto.institutionName())
                .street(addressDto.street())
                .city(addressDto.city())
                .buildingNumber(addressDto.buildingNumber())
                .postalCode(addressDto.postalCode())
                .countryCode(addressDto.countryCode())
                .latitude(addressDto.latitude())
                .longitude(addressDto.longitude())
                .parking(parking)
                .isBelongToAnyInstitution(addressDto.isBelongToAnyInstitution())
                .recordStatus(AbstractBaseEntity.RecordStatus.ACTIVE)
                .build();
        address = addressRepository.save(address);
        parking.setAddress(address);

        log.info("Address for parking: {} created", parking.getId());
        return AddressDto.fromEntity(address);
    }

    private String[] getNullPropertyNames(AddressDto addressDto) {
        return Arrays.stream(AddressDto.class.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> {
                    try {
                        Method getter = AddressDto.class.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                        return getter.invoke(addressDto) == null;
                    } catch (Exception e) {
                        return false;
                    }
                }).toArray(String[]::new);
    }
}
