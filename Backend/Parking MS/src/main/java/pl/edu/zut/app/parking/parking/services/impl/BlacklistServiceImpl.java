package pl.edu.zut.app.parking.parking.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.parking.clients.VehicleClient;
import pl.edu.zut.app.parking.parking.dto.VehicleDto;
import pl.edu.zut.app.parking.parking.dto.common.BlacklistDto;
import pl.edu.zut.app.parking.parking.entities.Blacklist;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.AccessListNotFoundException;
import pl.edu.zut.app.parking.parking.repositories.BlacklistRepository;
import pl.edu.zut.app.parking.parking.repositories.ParkingRepository;
import pl.edu.zut.app.parking.parking.services.BlacklistService;

import java.util.UUID;

import static pl.edu.zut.app.parking.parking.specifications.AccessListSpecification.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final ParkingRepository parkingRepository;
    private final VehicleClient vehicleClient;

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

    @Override
    public BlacklistDto add(UUID parkingId, BlacklistDto dto) {
        boolean exists = blacklistRepository.existsByParkingIdAndPlateNumber(parkingId, dto.plateNumber());
        if (exists) {
            throw new IllegalArgumentException("Vehicle with plate number '" + dto.plateNumber() + "' is already in the blacklist for this parking.");
        }

        VehicleDto vehicleDto = findOrCreateVehicle(dto.plateNumber());

        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new AccessListNotFoundException("Parking with id " + parkingId + " not found"));

        Blacklist blacklist = Blacklist.builder()
                .plateNumber(dto.plateNumber())
                .reason(dto.reason())
                .parking(parking)
                .vehicleId(vehicleDto.id())
                .build();

        return BlacklistDto.from(blacklistRepository.save(blacklist));
    }

    @Override
    public void delete(UUID accessListId) {
        if (!blacklistRepository.existsById(accessListId)) {
            throw new AccessListNotFoundException("Blacklist record not found");
        }
        blacklistRepository.deleteById(accessListId);
    }

    @Override
    public Page<BlacklistDto> find(UUID parkingId, Integer page, Integer size, String vehiclePlate, UUID accessListId, UUID vehicleId) {
        Specification<Blacklist> specification = (Specification<Blacklist>) (Specification<?>)
                Specification.where(whereIdEquals(accessListId))
                .and(whereVehicleIdEquals(vehicleId))
                .and(wherePlateNumberEquals(vehiclePlate))
                .and(whereParkingIdEquals(parkingId));
        return blacklistRepository.findAll(specification, PageRequest.of(page, size)).map(BlacklistDto::from);
    }

    @Override
    public Boolean check(UUID parkingId, String vehiclePlate) {
        return blacklistRepository.findByPlateNumberAndParkingId(vehiclePlate, parkingId).isPresent();
    }
}
