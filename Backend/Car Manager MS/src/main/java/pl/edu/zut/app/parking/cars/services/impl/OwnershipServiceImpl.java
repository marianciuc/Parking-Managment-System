package pl.edu.zut.app.parking.cars.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.zut.app.parking.cars.entities.Ownership;
import pl.edu.zut.app.parking.cars.entities.OwnershipChangeRequest;
import pl.edu.zut.app.parking.cars.entities.Vehicle;
import pl.edu.zut.app.parking.cars.exceptions.ForbiddenException;
import pl.edu.zut.app.parking.cars.exceptions.OwnershipNotFoundException;
import pl.edu.zut.app.parking.cars.exceptions.RequestNotFoundException;
import pl.edu.zut.app.parking.cars.exceptions.VehicleNotFoundException;
import pl.edu.zut.app.parking.cars.repositories.OwnershipChangeRequestRepository;
import pl.edu.zut.app.parking.cars.repositories.VehicleRepository;
import pl.edu.zut.app.parking.cars.services.OwnershipService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnershipServiceImpl implements OwnershipService {

    private final OwnershipChangeRequestRepository ownershipChangeRequestRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public Boolean requestOwnershipTransfer(UUID carId) {
        log.info("Entering requestOwnershipTransfer method with carId: {}", carId);
        UUID userId = SecurityContextUtil.extractUserIdFromSecurityContext();
        Vehicle vehicle = findVehicleOrThrow(carId);

        if (vehicle.getOwnership() == null) {
            log.info("Vehicle with id {} has no existing ownership. Proceeding with ownership transfer.", carId);
            return transferOwnership(carId, userId, Ownership.OwnerType.PERSON);
        }

        log.info("Vehicle with id {} already has ownership. Creating ownership transfer request.", vehicle.getId());
        return createOwnershipTransferRequest(vehicle, userId);
    }

    private Vehicle findVehicleOrThrow(UUID carId) {
        log.debug("Attempting to find vehicle with id: {}", carId);
        return vehicleRepository.findById(carId)
                .orElseThrow(() -> {
                    log.error("Vehicle with id {} not found", carId);
                    return new VehicleNotFoundException("Vehicle with id " + carId + " not found");
                });
    }

    private Boolean createOwnershipTransferRequest(Vehicle vehicle, UUID userId) {
        log.info("Creating ownership transfer request for vehicle with id: {} by user: {}", vehicle.getId(), userId);
        Ownership ownership = vehicle.getOwnership();

        if (ownership.getOwnerId().equals(userId)) {
            log.warn("User with id {} tried to create an ownership transfer request for their own vehicle with id {}", userId, vehicle.getId());
            throw new ForbiddenException("User already has ownership of vehicle with id " + vehicle.getId());
        }

        OwnershipChangeRequest request = OwnershipChangeRequest.builder()
                .vehicle(vehicle)
                .initiatorId(userId)
                .initiatorType(Ownership.OwnerType.PERSON)
                .ownerId(ownership.getOwnerId())
                .status(OwnershipChangeRequest.RequestStatus.PENDING)
                .build();

        log.info("Ownership transfer request created for vehicle id: {} by user id: {}", vehicle.getId(), userId);
        ownershipChangeRequestRepository.save(request);
        // TODO: send notification to owner
        return true;
    }

    @Override
    @Transactional
    public Boolean transferOwnership(UUID carId, UUID newOwnerId, Ownership.OwnerType ownerType) {
        log.info("Entering transferOwnership method with carId: {}, newOwnerId: {}, ownerType: {}", carId, newOwnerId, ownerType);
        Vehicle vehicle = findVehicleOrThrow(carId);

        if (vehicle.getOwnership() != null) {
            vehicle.setOwnership(null);
            log.debug("Removing existing ownership for vehicle with id: {}", carId);
            vehicle.setOwnership(null);
        }
        Ownership ownership = Ownership.builder()
                .vehicle(vehicle)
                .ownerId(newOwnerId)
                .ownerType(Ownership.OwnerType.PERSON)
                .recordStatus(Ownership.RecordStatus.ACTIVE)
                .build();

        vehicle.setOwnership(ownership);
        vehicle.setHasOwner(true);
        log.info("Ownership transferred to user id: {} for vehicle id: {}", newOwnerId, carId);
        vehicleRepository.save(vehicle);

        return true;
    }

    @Override
    @Transactional
    public void deleteOwnership(UUID carId) {
        log.info("Entering deleteOwnership method with carId: {}", carId);
        Vehicle vehicle = findVehicleOrThrow(carId);

        if (vehicle.getOwnership() != null) {
            log.debug("Removing ownership for vehicle with id: {}", carId);
            vehicle.removeOwnership();
            vehicle.setHasOwner(false);
            vehicleRepository.save(vehicle);
        } else {
            log.error("Attempted to delete ownership for vehicle with id {} but no ownership exists", carId);
            throw new OwnershipNotFoundException("Ownership associated to car with id " + carId + " not found");
        }
    }

    @Override
    @Transactional
    public Boolean acceptRequest(UUID requestId) {
        log.info("Entering acceptRequest method with requestId: {}", requestId);
        OwnershipChangeRequest request = ownershipChangeRequestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Ownership request with id {} not found", requestId);
            return new RequestNotFoundException("Request with id " + requestId + " not found");
        });

        request.setStatus(OwnershipChangeRequest.RequestStatus.ACCEPTED);
        request.setReactionDate(java.time.LocalDateTime.now());
        request.setReactedBy(SecurityContextUtil.extractUserIdFromSecurityContext());
        ownershipChangeRequestRepository.save(request);

        log.info("Accepting ownership request for requestId: {}. Transferring ownership for vehicle id: {}", requestId, request.getVehicle().getId());
        transferOwnership(request.getVehicle().getId(), request.getInitiatorId(), request.getInitiatorType());
        transferOwnership(request.getVehicle().getId(), request.getInitiatorId(), request.getInitiatorType());
        return true;
    }

    @Override
    public Boolean rejectRequest(UUID requestId) {
        log.info("Entering rejectRequest method with requestId: {}", requestId);
        OwnershipChangeRequest request = ownershipChangeRequestRepository.findById(requestId).orElseThrow(() -> {
            log.error("Ownership request with id {} not found during rejection process", requestId);
            return new RequestNotFoundException("Request with id " + requestId + " not found");
        });
        if (request.getStatus() == OwnershipChangeRequest.RequestStatus.PENDING) {
            request.setStatus(OwnershipChangeRequest.RequestStatus.DECLINED);
            request.setReactionDate(java.time.LocalDateTime.now());
            request.setReactedBy(SecurityContextUtil.extractUserIdFromSecurityContext());
            ownershipChangeRequestRepository.save(request);
            return true;
        }
        throw new RequestNotFoundException("Request with id " + requestId + " is not pending");
    }

    private void validateRequestStatus(OwnershipChangeRequest request, OwnershipChangeRequest.RequestStatus
            expectedStatus) {
        if (!request.getStatus().equals(expectedStatus)) {
            throw new RequestNotFoundException("Request with id " + request.getId() + " is not in expected status: " + expectedStatus);
        }
    }

}
