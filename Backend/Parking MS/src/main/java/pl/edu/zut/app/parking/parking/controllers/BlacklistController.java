package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnershipOrAdmin;
import pl.edu.zut.app.parking.parking.dto.common.BlacklistDto;
import pl.edu.zut.app.parking.parking.services.BlacklistService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/parking/{parkingId}/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService blacklistService;

    @GetMapping("/find")
    public ResponseEntity<Page<BlacklistDto>> getBlacklist(@PathVariable UUID parkingId,
                                                           @RequestParam(name = "page", required = false,
                                                                   defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", required = false,
                                                                   defaultValue = "10") Integer size,
                                                           @RequestParam(name = "plate",
                                                                   required = false) String vehiclePlate,
                                                           @RequestParam(name = "blacklistId",
                                                                   required = false) UUID blacklistId,
                                                           @RequestParam(name = "vehicleId",
                                                                   required = false) UUID vehicleId) {
        log.info("Searching for blacklist with id: {} and plate: {}", blacklistId, vehiclePlate);
        return ResponseEntity.ok(blacklistService.find(parkingId, page, size, vehiclePlate, blacklistId, vehicleId));
    }

    @PostMapping
    @CheckOwnershipOrAdmin
    public ResponseEntity<BlacklistDto> addBlacklist(@PathVariable UUID parkingId, @RequestBody BlacklistDto blackListDto) {
        return ResponseEntity.ok(blacklistService.add(parkingId, blackListDto));
    }

    @DeleteMapping("/{blacklistId}")
    @CheckOwnershipOrAdmin
    public ResponseEntity<Void> deleteBlacklist(@PathVariable UUID parkingId, @PathVariable UUID blacklistId) {
        blacklistService.delete(blacklistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkBlacklist(@PathVariable UUID parkingId,
                                                       @RequestParam(name = "vehiclePlate") String vehiclePlate) {
        return ResponseEntity.ok(blacklistService.check(parkingId, vehiclePlate));
    }

}
