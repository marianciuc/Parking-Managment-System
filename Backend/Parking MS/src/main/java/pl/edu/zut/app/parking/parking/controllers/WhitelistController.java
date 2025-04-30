package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnership;
import pl.edu.zut.app.parking.parking.dto.common.WhitelistDto;
import pl.edu.zut.app.parking.parking.services.WhitelistService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking/{parkingId}/whitelist")
@RequiredArgsConstructor
public class WhitelistController {

    private final WhitelistService whitelistService;


    @GetMapping("/find")
    public ResponseEntity<Page<WhitelistDto>> getWhitelist(@PathVariable UUID parkingId,
                                                           @RequestParam(name = "page", required = false,
                                                                   defaultValue = "0") Integer page,
                                                           @RequestParam(name = "size", required = false,
                                                                   defaultValue = "10") Integer size,
                                                           @RequestParam(name = "vehiclePlate",
                                                                   required = false) String vehiclePlate,
                                                           @RequestParam(name = "blacklistId",
                                                                   required = false) UUID blacklistId,
                                                           @RequestParam(name = "vehicleId",
                                                                   required = false) UUID vehicleId) {
        return ResponseEntity.ok(whitelistService.find(parkingId, page, size, vehiclePlate, blacklistId, vehicleId));
    }

    @PostMapping
    @CheckOwnership
    public ResponseEntity<WhitelistDto> addWhitelist(@PathVariable UUID parkingId, @RequestBody WhitelistDto req) {
        return ResponseEntity.ok(whitelistService.add(parkingId, req));
    }

    @DeleteMapping("/{whitelistId}")
    @CheckOwnership
    public ResponseEntity<Void> deleteWhitelist(@PathVariable UUID parkingId, @PathVariable UUID whitelistId) {
        whitelistService.delete(whitelistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkWhitelist(@PathVariable UUID parkingId,
                                                       @RequestParam(name = "vehiclePlate") String vehiclePlate) {
        return ResponseEntity.ok(whitelistService.check(parkingId, vehiclePlate));
    }




}
