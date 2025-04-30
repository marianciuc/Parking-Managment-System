package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnershipOrAdmin;
import pl.edu.zut.app.parking.parking.dto.common.GateDto;
import pl.edu.zut.app.parking.parking.services.GateControlService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gates")
@RequiredArgsConstructor
public class GateController {

    private final GateControlService gateControlService;

    @PostMapping("/parking/{parkingId}/gate/{gateId}/close")
    @CheckOwnershipOrAdmin
    public ResponseEntity<Void> closeGate(@PathVariable UUID parkingId, @PathVariable UUID gateId) {
        gateControlService.closeGate(gateId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/parking/{parkingId}/gate/{gateId}/open")
    @CheckOwnershipOrAdmin
    public ResponseEntity<Void> openGate(@PathVariable UUID parkingId, @PathVariable UUID gateId) {
        gateControlService.openGate(gateId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('PARKING')")
    public ResponseEntity<GateDto> register(@RequestBody GateDto request) {
        return ResponseEntity.ok(gateControlService.registerGate(request));
    }

    @PostMapping("/unregister")
    @PreAuthorize("hasAuthority('PARKING')")
    public ResponseEntity<Void> unregister(@RequestParam UUID gateId) {
        gateControlService.unregisterGate(gateId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parking/{parkingId}")
    @CheckOwnershipOrAdmin
    public ResponseEntity<List<GateDto>> getGates(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(gateControlService.getGatesByParkingId(parkingId));
    }

    @PutMapping("/parking/{parkingId}/gate/{gateId}")
    @CheckOwnershipOrAdmin
    public ResponseEntity<Void> changeManualMode(@PathVariable UUID parkingId, @PathVariable UUID gateId,
                                                   @RequestParam Boolean manualMode) {
        gateControlService.changeManualMode(gateId, manualMode);
        return ResponseEntity.ok().build();
    }
}
