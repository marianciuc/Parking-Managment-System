package pl.edu.zut.app.parking.parking.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.parking.clients.TariffServiceClient;
import pl.edu.zut.app.parking.parking.clients.VehicleClient;
import pl.edu.zut.app.parking.parking.dto.TariffDto;
import pl.edu.zut.app.parking.parking.dto.VehicleDto;
import pl.edu.zut.app.parking.parking.dto.common.WhitelistDto;
import pl.edu.zut.app.parking.parking.entities.AbstractVehicleList;
import pl.edu.zut.app.parking.parking.entities.Blacklist;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.entities.Whitelist;
import pl.edu.zut.app.parking.parking.exceptions.AccessListNotFoundException;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;
import pl.edu.zut.app.parking.parking.repositories.ParkingRepository;
import pl.edu.zut.app.parking.parking.repositories.WhitelistRepository;
import pl.edu.zut.app.parking.parking.services.WhitelistService;
import pl.edu.zut.app.parking.parking.specifications.AccessListSpecification;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class WhitelistServiceImpl implements WhitelistService {

    private final WhitelistRepository whitelistRepository;
    private final ParkingRepository parkingRepository;
    private final VehicleClient vehicleClient;
    private final TariffServiceClient tariffServiceClient;

    private VehicleDto findOrCreateVehicle(String plate) {
        try {
            ResponseEntity<VehicleDto> response = vehicleClient.findByPlate(plate);
            return response.getBody();
        } catch (Exception exception) {
            log.error("Error fetching vehicle by plate number");
            return createVehicleOrThrow(plate);
        }
    }

    private VehicleDto createVehicleOrThrow(String plate) {
        ResponseEntity<VehicleDto> createdResponse =
                vehicleClient.create(VehicleDto.buildWithPlateNumber(plate), false);
        if (createdResponse.getStatusCode().isError() || createdResponse.getBody() == null) {
            throw new IllegalArgumentException("Error creating vehicle.");
        }
        return createdResponse.getBody();
    }

    private TariffDto getTarrifOrThrow(UUID parkingId, UUID tarriffId) {
        ResponseEntity<TariffDto> createdResponse =
                tariffServiceClient.getTariffById(tarriffId, parkingId, "USD");
        if (createdResponse.getStatusCode().isError() || createdResponse.getBody() == null) {
            throw new IllegalArgumentException("Error creating vehicle.");
        }
        return createdResponse.getBody();
    }

    @Override
    public WhitelistDto add(UUID parkingId, WhitelistDto dto) {
        boolean exists = whitelistRepository.existsByParkingIdAndPlateNumber(parkingId, dto.vehiclePlate());
        if (exists) {
            throw new IllegalArgumentException("Vehicle with plate number '" + dto.vehiclePlate() + "' is already in " +
                    "the whitelist for this parking.");
        }

        VehicleDto vehicleDto = findOrCreateVehicle(dto.vehiclePlate());

        TariffDto tariffDto = getTarrifOrThrow(parkingId, dto.tariffId());

        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new AccessListNotFoundException("Parking with id " + parkingId + " not found"));

        Whitelist record = Whitelist.builder()
                .plateNumber(dto.vehiclePlate())
                .tariffId(dto.tariffId())
                .tariffName(tariffDto.name())
                .parking(parking)
                .vehicleId(vehicleDto.id())
                .build();

        return WhitelistDto.from(whitelistRepository.save(record));
    }

    @Override
    public void delete(UUID accessListId) {
        if (!whitelistRepository.existsById(accessListId)) throw new NotFoundException("Whitelist record not found");
        whitelistRepository.deleteById(accessListId);
    }

    @Override
    public Page<WhitelistDto> find(UUID parkingId, Integer page, Integer size, String vehiclePlate, UUID accessListId, UUID vehicleId) {
        Specification<Whitelist> specification = (Specification<Whitelist>) (Specification<?>) Specification
                .where(AccessListSpecification.whereIdEquals(accessListId))
                .and(AccessListSpecification.whereVehicleIdEquals(vehicleId))
                .and(AccessListSpecification.wherePlateNumberEquals(vehiclePlate))
                .and(AccessListSpecification.whereParkingIdEquals(parkingId));

        return whitelistRepository.findAll(specification, PageRequest.of(page, size)).map(WhitelistDto::from);
    }

    @Override
    public Boolean check(UUID parkingId, String vehiclePlate) {
        return whitelistRepository.existsByParkingIdAndPlateNumber(parkingId, vehiclePlate);
    }

    @Override
    public WhitelistDto getWhitelistByVehicleIdAndParkingId(UUID vehicleId, UUID parkingId) {
        Whitelist whitelist = whitelistRepository.findByVehicleIdAndParkingId(vehicleId, parkingId)
                .orElseThrow(() -> new NotFoundException("Whitelist record not found"));
        return WhitelistDto.from(whitelist);
    }

    @Override
    public Boolean check(UUID parkingId, UUID vehicleId) {
        Whitelist whitelist = whitelistRepository.findByVehicleIdAndParkingId(vehicleId, parkingId)
                .orElse(null);
        return whitelist != null;
    }
}
