package pl.edu.zut.app.parking.parking.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import pl.edu.zut.app.parking.parking.dto.TariffDto;
import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingCreateRequest;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingDetailsRequest;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingListItemDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingUpdateRequest;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.enums.ParkingStatus;

import java.util.List;
import java.util.UUID;

/**
 * Interface representing the operations for managing parking entities. This service provides functionality
 * to create, update, delete, and retrieve parking details along with managing their associated parameters like
 * tags, operating hours, access types, and statuses.
 */
public interface ParkingService {

    /**
     * Creates a new parking entity based on the details provided in the request.
     *
     * @param request an object of type ParkingCreateRequest containing the details required to create a parking
     *                entity, such as its name and address
     * @return an object of type ParkingDetailsResponse representing the newly created parking entity,
     * including its details such as ID, status, capacity, and other related information
     * @throws pl.edu.zut.app.parking.parking.exceptions.ForbiddenException if the user is not authorized to create a parking entity
     */
    ParkingDetailsResponse createParking(ParkingCreateRequest request);

    ParkingDetailsResponse updateTags(UUID parkingId, List<UUID> tags);

    ParkingDetailsResponse updateParkingDetails(UUID parkingId, ParkingUpdateRequest parkingDetails);

    void updateOpeningHours(UUID parkingId, boolean is24, List<OpeningHoursDto> workingHours);

    void deleteDetailsByParkingId(UUID parkingId);

    Page<ParkingListItemDto> findParkingDetails(Integer page, Integer size, String sort, String sortBy, Boolean includeHidden, @Valid ParkingDetailsRequest request);

    void updateAccessType(UUID parkingId, AccessType accessType);

    List<ParkingDetailsResponse> findParkingDetailsByOwnerId(UUID id);

    Boolean isParkingAvailable(UUID parkingId);

    boolean isOwner(UUID parkingId, UUID id);

    void updateParkingStatus(UUID parkingId, ParkingStatus status);

    Boolean isAccessAllowed(UUID parkingId, UUID vehicleId);

    ParkingDetailsResponse findParkingDetailsById(UUID parkingId);

    void updateRating(UUID parkingId, double rating, int ratingCount);

    TariffDto getActiveTariffForVehicle(UUID parkingId, UUID vehicleId, long minutes, String currency);

    ParkingDetailsResponse temporaryClose(UUID parkingId);

    void unpin(UUID parkingId);

    void pin(UUID parkingId);

    ParkingDetailsResponse open(UUID parkingId);

    String getParkingCurrency(UUID parkingId);

    ParkingDetailsResponse updateParkingAddress(UUID parkingId, AddressDto address);
}
