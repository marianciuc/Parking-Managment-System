package pl.edu.zut.app.parking.parking.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.zut.app.parking.parking.clients.PaymentClient;
import pl.edu.zut.app.parking.parking.clients.TariffServiceClient;
import pl.edu.zut.app.parking.parking.dto.TariffDto;
import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.dto.common.WhitelistDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingCreateRequest;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingDetailsRequest;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingListItemDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingUpdateRequest;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.entities.AbstractBaseEntity;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.entities.Tag;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;
import pl.edu.zut.app.parking.parking.exceptions.NotFoundException;
import pl.edu.zut.app.parking.parking.kafka.ParkingCreatedMessageProducer;
import pl.edu.zut.app.parking.parking.repositories.ParkingRepository;
import pl.edu.zut.app.parking.parking.services.*;
import pl.edu.zut.app.parking.parking.specifications.ParkingSpecifications;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParkingDetailsServiceImpl implements ParkingService {

    private final ParkingRepository parkingRepository;
    private final OpeningHoursService openingHoursService;
    private final TagService tagService;
    private final AddressService addressService;
    private final CapacityService capacityService;
    private final WhitelistService whitelistService;
    private final TariffServiceClient tariffServiceClient;
    private final ParkingCreatedMessageProducer parkingCreatedMessageProducer;
    private final PaymentClient paymentClient;

    private Parking getParkingById(UUID parkingId) {
        return parkingRepository.findByIdAndRecordStatusIsNot(parkingId, AbstractBaseEntity.RecordStatus.DELETED)
                .orElseThrow(() -> new NotFoundException("Parking not found with id: " + parkingId));
    }

    private void validateParkingStatus(Parking parking) {
        if (parking.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED ||
                parking.getStatus() == ParkingStatus.ARCHIVED) {
            throw new IllegalArgumentException("Parking is either deleted or archived.");
        }
    }

    private Parking applyChanges(UUID parkingId, Consumer<Parking> updater) {
        Parking parking = getParkingById(parkingId);
        updater.accept(parking);
        return parkingRepository.save(parking);
    }

    @Override
    @Transactional
    public ParkingDetailsResponse createParking(ParkingCreateRequest request) {
        Parking newParking = Parking.builder()
                .ownerId(SecurityContextUtil.extractUserIdFromSecurityContext())
                .name(request.name())
                .recordStatus(AbstractBaseEntity.RecordStatus.ACTIVE)
                .status(ParkingStatus.CREATION_PROCESS)
                .accessType(AccessType.OPEN)
                .is24h(true)
                .rating(0).reviewsCount(0)
                .tags(new ArrayList<>())
                .blacklists(new ArrayList<>())
                .whitelists(new ArrayList<>())
                .build();

        parkingRepository.save(newParking);
        addressService.create(newParking, request.address());
        capacityService.createCapacity(newParking);
        openingHoursService.create24HourOpeningHours(newParking);
        parkingCreatedMessageProducer.sendParkingCreatedMessage(newParking.getId());

        return ParkingDetailsResponse.fromEntity(newParking);
    }

    @Override
    @Transactional
    public ParkingDetailsResponse updateParkingDetails(UUID parkingId, ParkingUpdateRequest details) {
        return ParkingDetailsResponse.fromEntity(
                applyChanges(parkingId, parking -> {
                    Optional.ofNullable(details.name()).ifPresent(parking::setName);
                    Optional.ofNullable(details.imageUrl()).ifPresent(parking::setImageUrl);
                    Optional.ofNullable(details.description()).ifPresent(parking::setDescription);
                })
        );
    }

    @Override
    @Transactional
    public void deleteDetailsByParkingId(UUID parkingId) {
        Parking parking = getParkingById(parkingId);
        if (parking.getRecordStatus() == AbstractBaseEntity.RecordStatus.DELETED) {
            throw new IllegalArgumentException("Parking is already deleted.");
        }

        if (parking.getStatus() == ParkingStatus.CREATION_PROCESS) {
            addressService.deletePermanent(parking.getAddress().getId());
            openingHoursService.deleteOpeningHours(parking);
            parkingRepository.delete(parking);
        } else {
            applyChanges(parkingId, p -> {
                p.setStatus(ParkingStatus.ARCHIVED);
                p.setRecordStatus(AbstractBaseEntity.RecordStatus.DELETED);
            });
        }
    }

    @Override
    @Transactional
    public ParkingDetailsResponse updateTags(UUID parkingId, List<UUID> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Tags cannot be null or empty.");
        }

        Parking parking = getParkingById(parkingId);
        List<Tag> validTags = tagService.getTagsByIds(tags);

        if (validTags.size() != tags.size()) {
            throw new NotFoundException("Some provided tags do not exist.");
        }

        parking.setTags(validTags);
        return ParkingDetailsResponse.fromEntity(parkingRepository.save(parking));
    }

    @Override
    public Page<ParkingListItemDto> findParkingDetails(Integer page, Integer size, String sort, String sortBy,
                                                       Boolean includeHidden, ParkingDetailsRequest request) {
        Specification<Parking> spec = ParkingSpecifications.fromDetailsRequest(request);
        return parkingRepository.findAll(spec, PageRequest.of(page, size))
                .map(ParkingListItemDto::fromEntity);
    }

    @Override
    public void updateAccessType(UUID parkingId, AccessType accessType) {

    }

    @Override
    public boolean isOwner(UUID parkingId, UUID userId) {
        return getParkingById(parkingId).getOwnerId().equals(userId);
    }

    @Override
    public void updateParkingStatus(UUID parkingId, ParkingStatus status) {
        applyChanges(parkingId, p -> p.setStatus(status));
    }

    @Override
    public Boolean isParkingAvailable(UUID parkingId) {
        return getParkingById(parkingId).getRecordStatus() == AbstractBaseEntity.RecordStatus.ACTIVE;
    }

    @Override
    public Boolean isAccessAllowed(UUID parkingId, UUID vehicleId) {
        Parking parking = getParkingById(parkingId);
        return isParkingActive(parking)
                && (parking.getAccessType() != AccessType.WHITELIST_ONLY || checkWhitelist(parking, vehicleId))
                && !isVehicleBlacklisted(parking, vehicleId);
    }

    @Override
    public ParkingDetailsResponse findParkingDetailsById(UUID parkingId) {
        return ParkingDetailsResponse.fromEntity(getParkingById(parkingId));
    }


    private boolean isParkingActive(Parking parking) {
        return parking.getRecordStatus() == AbstractBaseEntity.RecordStatus.ACTIVE
                && !EnumSet.of(ParkingStatus.CLOSED, ParkingStatus.CREATION_PROCESS, ParkingStatus.TEMPORARY_CLOSED)
                .contains(parking.getStatus());
    }

    private boolean isVehicleBlacklisted(Parking parking, UUID vehicleId) {
        return parking.getBlacklists().stream()
                .anyMatch(blacklist -> blacklist.getVehicleId().equals(vehicleId));
    }

    private boolean checkWhitelist(Parking parking, UUID vehicleId) {
        return parking.getWhitelists().stream()
                .anyMatch(whitelist -> whitelist.getVehicleId().equals(vehicleId));
    }

    @Override
    public ParkingDetailsResponse temporaryClose(UUID parkingId) {
        return ParkingDetailsResponse.fromEntity(
                applyChanges(parkingId, parking -> parking.setStatus(ParkingStatus.TEMPORARY_CLOSED))
        );
    }

    @Override
    public void unpin(UUID parkingId) {
        Parking parking = getParkingById(parkingId);
        isParkingActive(parking);
        if (parking.isPinned()) {
            applyChanges(parkingId, p -> p.setPinned(false));
        } else {
            throw new IllegalArgumentException("Parking is not pinned.");
        }
    }

    @Override
    public void pin(UUID parkingId) {
        Parking parking = getParkingById(parkingId);
        isParkingActive(parking);
        if (!parking.isPinned()) {
            applyChanges(parkingId, p -> p.setPinned(true));
        } else {
            throw new IllegalArgumentException("Parking is already pinned.");
        }
    }

    @Override
    public ParkingDetailsResponse open(UUID parkingId) {
        Parking parking = getParkingById(parkingId);

        if (parking.getStatus().equals(ParkingStatus.TEMPORARY_CLOSED)) {
            parking.setStatus(ParkingStatus.OPEN);
            // set status by openHoursService decision
            return ParkingDetailsResponse.fromEntity(parkingRepository.save(parking));
        } else {
            throw new IllegalArgumentException("Parking is not temporary closed.");
        }
    }

    @Override
    public String getParkingCurrency(UUID parkingId) {
        Parking parking = getParkingById(parkingId);
        ResponseEntity<String> currencyResponseEntity = paymentClient.getAccountCurrency(parking.getOwnerId());

        if (currencyResponseEntity.getStatusCode().is2xxSuccessful()) {
            return currencyResponseEntity.getBody();
        } else {
            throw new NotFoundException("Parking currency not found");
        }
    }

    @Override
    public ParkingDetailsResponse updateParkingAddress(UUID parkingId, AddressDto address) {
        Parking parking = getParkingById(parkingId);

        if (parking.getStatus().equals(ParkingStatus.CREATION_PROCESS)) {
            addressService.update(parkingId, address);
            return ParkingDetailsResponse.fromEntity(getParkingById(parkingId));
        } else {
            throw new IllegalArgumentException("Parking is not in creation process.");
        }
    }


    @Override
    public void updateRating(UUID parkingId, double rating, int ratingCount) {
        if (Math.abs(ratingCount) != 1) {
            throw new IllegalArgumentException("Rating count must be 1 or -1.");
        }

        applyChanges(parkingId, parking -> {
            double totalRating = parking.getRating() * parking.getReviewsCount() + rating * ratingCount;
            parking.setReviewsCount(parking.getReviewsCount() + ratingCount);
            parking.setRating(totalRating / Math.max(1, parking.getReviewsCount()));
        });
    }

    @Override
    public TariffDto getActiveTariffForVehicle(UUID parkingId, UUID vehicleId, long minutes, String currency) {
        Parking parking = parkingRepository.findById(parkingId).orElseThrow(() -> new NotFoundException("Parking not found"));
        if (checkWhitelist(parking, vehicleId)) {
            WhitelistDto whitelistDto = whitelistService.getWhitelistByVehicleIdAndParkingId(vehicleId, parkingId);
            try {
                ResponseEntity<TariffDto> tariffDtoResponseEntity =
                        tariffServiceClient.getTariffById(whitelistDto.tariffId(), parkingId, currency);
                return tariffDtoResponseEntity.getBody();
            } catch (Exception e) {
                log.error("Tariff not found for whitelist with id: {}", whitelistDto.id());
                throw new NotFoundException("Tariff not found for whitelist with id: " + whitelistDto.id());
            }
        }
        try {
            ResponseEntity<List<TariffDto>> tariffDtoResponseEntity = tariffServiceClient.getTariff(parkingId,
                    minutes, currency);
            if (Objects.requireNonNull(tariffDtoResponseEntity.getBody()).isEmpty()) {
                log.error("Tariff not found for parking with id: {}, res: {}", parkingId, tariffDtoResponseEntity);
                throw new NotFoundException("Tariff not found for parking with id: " + parkingId);
            } else {
                return tariffDtoResponseEntity.getBody().getFirst();
            }
        } catch (Exception e) {
            log.error("Tariff not found for parking with id: {}", parkingId, e);
            throw new NotFoundException("Tariff not found for parking with id: " + parkingId);
        }
    }

    @Override
    public List<ParkingDetailsResponse> findParkingDetailsByOwnerId(UUID ownerId) {
        return parkingRepository.findAllByOwnerIdAndRecordStatusIsNot(ownerId, AbstractBaseEntity.RecordStatus.DELETED).stream()
                .map(ParkingDetailsResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateOpeningHours(UUID parkingId, boolean is24, List<OpeningHoursDto> hours) {
        Parking parking = getParkingById(parkingId);
        openingHoursService.updateOpeningHours(parking, is24, hours);
        parkingRepository.save(parking);
    }
}