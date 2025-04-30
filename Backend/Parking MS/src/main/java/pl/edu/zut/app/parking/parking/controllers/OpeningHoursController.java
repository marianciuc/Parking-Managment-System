package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.annotations.CheckOwnership;
import pl.edu.zut.app.parking.parking.dto.common.OpeningHoursDto;
import pl.edu.zut.app.parking.parking.services.OpeningHoursService;
import pl.edu.zut.app.parking.parking.services.ParkingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking/{parkingId}/opening-hours")
@RequiredArgsConstructor
public class OpeningHoursController {

    private final OpeningHoursService openingHoursService;
    private final ParkingService parkingService;

    @PutMapping
    @CheckOwnership
    public ResponseEntity<Void> updateOpeningHours(@PathVariable UUID parkingId,
                                             @RequestParam(value = "is24h", required = true) Boolean is24h,
                                             @RequestBody List<OpeningHoursDto> workingHours) {
        parkingService.updateOpeningHours(parkingId, is24h, workingHours);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OpeningHoursDto>> getOpeningHours(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(openingHoursService.getOpeningHours(parkingId));
    }
}
