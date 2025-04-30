package pl.edu.zut.app.parking.owners.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.owners.dto.AddressDto;
import pl.edu.zut.app.parking.owners.dto.ParkingOwnerDto;
import pl.edu.zut.app.parking.owners.services.OwnerRepositoryService;
import pl.edu.zut.app.parking.owners.services.OwnerService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;
  private final OwnerRepositoryService ownerRepositoryService;

  @GetMapping("/check-permission")
  @PreAuthorize("hasRole('LOGIN_IN_PARKING_PANEL')")
  public ResponseEntity<Void> checkOwnerPermissions() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/details")
  public ResponseEntity<ParkingOwnerDto> getOwner() {
    ParkingOwnerDto owner =
        ParkingOwnerDto.fromEntity(
            ownerRepositoryService.findOwnerById(
                SecurityContextUtil.extractUserIdFromSecurityContext()));
    return ResponseEntity.ok(owner);
  }

  @GetMapping("/details/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> getOwnerDetails(
      @RequestParam UUID ownerId,
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam String nip,
      @RequestParam String phoneNumber,
      @RequestParam String countryCode) {
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{userId}/address")
  public ResponseEntity<Void> updateOwnerAddress(
      @RequestBody AddressDto req, @PathVariable UUID userId) {
    ownerService.updateOwnerAddress(userId, req);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{userId}/details")
  public ResponseEntity<ParkingOwnerDto> updateOwnerDetails(
      @PathVariable UUID userId, @RequestBody ParkingOwnerDto req) {
    return ResponseEntity.ok(ownerService.updateOwner(userId, req));
  }

  @GetMapping("/{userId}/details")
  public ResponseEntity<ParkingOwnerDto> updateOwnerDetails(@PathVariable UUID userId) {
    return ResponseEntity.ok(ParkingOwnerDto.fromEntity(ownerRepositoryService.findOwnerById(userId)));
  }
}
