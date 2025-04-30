package pl.edu.zut.app.parking.cars.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.cars.dto.OwnershipTransferRequest;
import pl.edu.zut.app.parking.cars.entities.Ownership;
import pl.edu.zut.app.parking.cars.services.OwnershipService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles/{vehicleId}/ownership")
@RequiredArgsConstructor
public class OwnershipController {

    private final OwnershipService ownershipService;

    @PostMapping("/transfer/request")
    public ResponseEntity<Boolean> transferOwnershipRequest(@PathVariable UUID vehicleId) {
        return ResponseEntity.ok(ownershipService.requestOwnershipTransfer(vehicleId));
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> transferOwnership(@PathVariable UUID vehicleId, @RequestParam UUID newOwnerId, @RequestParam String ownerType) {
        return ResponseEntity.ok(ownershipService.transferOwnership(vehicleId, newOwnerId, Ownership.OwnerType.valueOf(ownerType)));
    }

    @PutMapping("/{requestId}/accept")
    public ResponseEntity<Boolean> acceptOwnershipRequest(@PathVariable UUID vehicleId, @PathVariable UUID requestId) {
        return ResponseEntity.ok(ownershipService.acceptRequest(requestId));
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<Boolean> rejectOwnershipRequest(@PathVariable UUID vehicleId, @PathVariable UUID requestId) {
        return ResponseEntity.ok(ownershipService.rejectRequest(requestId));
    }
}
