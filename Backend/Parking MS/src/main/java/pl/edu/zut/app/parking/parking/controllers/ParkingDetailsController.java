package pl.edu.zut.app.parking.parking.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnership;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnershipOrAdmin;
import pl.edu.zut.app.parking.parking.dto.TariffDto;
import pl.edu.zut.app.parking.parking.dto.common.AddressDto;
import pl.edu.zut.app.parking.parking.dto.common.ParkingCapacityDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingDetailsRequest;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingListItemDto;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingUpdateRequest;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.enums.AccessType;
import pl.edu.zut.app.parking.parking.services.CapacityService;
import pl.edu.zut.app.parking.parking.services.ParkingService;
import pl.edu.zut.app.parking.parking.services.WhitelistService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking")
@RequiredArgsConstructor
public class ParkingDetailsController {

  private final ParkingService parkingService;
  private final CapacityService capacityService;
  private final WhitelistService whitelistService;

  @GetMapping("/search")
  public ResponseEntity<Page<ParkingListItemDto>> findParkingDetails(
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
      @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
      @RequestParam(name = "direction", required = false, defaultValue = "name") String sortBy,
      @RequestParam(name = "includeHidden", required = false) Boolean includeHidden,
      @ModelAttribute ParkingDetailsRequest request) {
    return ResponseEntity.ok(
        parkingService.findParkingDetails(page, size, sort, sortBy, includeHidden, request));
  }

  @PatchMapping("/{parkingId}/pin")
  @CheckOwnership
  public ResponseEntity<Void> updateFavorite(@PathVariable UUID parkingId) {
    parkingService.pin(parkingId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{parkingId}/unpin")
  @CheckOwnership
  public ResponseEntity<Void> removeFavorite(@PathVariable UUID parkingId) {
    parkingService.unpin(parkingId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{parkingId}/capacity")
  @CheckOwnershipOrAdmin
  public ResponseEntity<ParkingCapacityDto> updateCapacity(
      @PathVariable UUID parkingId, @RequestBody ParkingCapacityDto request) {
    return ResponseEntity.ok(capacityService.updateCapacityDetails(parkingId, request));
  }

  @GetMapping
  @PreAuthorize("hasRole('PARKING_OWNER')")
  public ResponseEntity<List<ParkingDetailsResponse>> findParkingDetailsByOwnerId() {
    return ResponseEntity.ok(
        parkingService.findParkingDetailsByOwnerId(
            SecurityContextUtil.extractUserIdFromSecurityContext()));
  }

  @PutMapping("/{parkingId}/tags")
  public void updateTags(@PathVariable UUID parkingId, @RequestBody List<UUID> tags) {
    parkingService.updateTags(parkingId, tags);
  }

  @PutMapping("/{parkingId}/access-type")
  public void updateStatus(@PathVariable UUID parkingId, @RequestBody AccessType accessType) {
    parkingService.updateAccessType(parkingId, accessType);
  }

  @DeleteMapping("/{parkingId}")
  @CheckOwnership
  public void deleteDetailsByParkingId(@PathVariable UUID parkingId) {
    parkingService.deleteDetailsByParkingId(parkingId);
  }

  @PutMapping("/{parkingId}")
  @CheckOwnership
  public ResponseEntity<ParkingDetailsResponse> updateParkingDetails(
      @PathVariable UUID parkingId, @RequestBody ParkingUpdateRequest request) {
    return ResponseEntity.ok(parkingService.updateParkingDetails(parkingId, request));
  }

  @GetMapping("/{parkingId}/access/{vehicleId}")
  public ResponseEntity<Boolean> isParkingAvailable(
      @PathVariable UUID parkingId, @PathVariable UUID vehicleId) {
    return ResponseEntity.ok(parkingService.isAccessAllowed(parkingId, vehicleId));
  }

  @PutMapping("/{parkingId}/status/temporary-close")
  @CheckOwnershipOrAdmin
  public ResponseEntity<ParkingDetailsResponse> updateStatus(@PathVariable UUID parkingId) {
    return ResponseEntity.ok(parkingService.temporaryClose(parkingId));
  }

  @PutMapping("/{parkingId}/status/open")
  @CheckOwnershipOrAdmin
  public ResponseEntity<ParkingDetailsResponse> openStatusParking(@PathVariable UUID parkingId) {
    return ResponseEntity.ok(parkingService.open(parkingId));
  }

  @GetMapping("/{parkingId}")
  public ResponseEntity<ParkingDetailsResponse> findParkingDetailsById(
      @PathVariable UUID parkingId) {
    return ResponseEntity.ok(parkingService.findParkingDetailsById(parkingId));
  }

  @GetMapping("/{parkingId}/exists")
  public ResponseEntity<Boolean> parkingExists(@PathVariable UUID parkingId) {
    return ResponseEntity.ok(parkingService.isParkingAvailable(parkingId));
  }

  @GetMapping("/{parkingId}/active-tariff")
  public ResponseEntity<TariffDto> getActiveTariff(
      @PathVariable UUID parkingId,
      @RequestParam(name = "vehicleId") UUID vehicleId,
      @RequestParam(name = "minutes") long minutes,
      @RequestParam(required = false) String currency) {
    return ResponseEntity.ok(
        parkingService.getActiveTariffForVehicle(parkingId, vehicleId, minutes, currency));
  }

  @GetMapping("/{parkingId}/default-currency")
  ResponseEntity<String> getParkingDefaultCurrency(@PathVariable UUID parkingId) {
    return ResponseEntity.ok(parkingService.getParkingCurrency(parkingId));
  }

  @GetMapping("/{parkingId}/access/whitelist/{vehicleId}")
  ResponseEntity<Boolean> isInWhitelist(
      @PathVariable UUID parkingId, @PathVariable UUID vehicleId) {
    return ResponseEntity.ok(whitelistService.check(parkingId, vehicleId));
  }

  @PutMapping("/{parkingId}/address")
  @CheckOwnershipOrAdmin
  ResponseEntity<ParkingDetailsResponse> updateParkingAddress(
      @PathVariable UUID parkingId, @RequestBody AddressDto address) {
    return ResponseEntity.ok(parkingService.updateParkingAddress(parkingId, address));
  }

  @GetMapping("/{parkingId}/owner-id")
  ResponseEntity<UUID> getParkingOwnerId(@PathVariable UUID parkingId) {
      return ResponseEntity.ok(parkingService.findParkingDetailsById(parkingId).ownerId());
  }
}
