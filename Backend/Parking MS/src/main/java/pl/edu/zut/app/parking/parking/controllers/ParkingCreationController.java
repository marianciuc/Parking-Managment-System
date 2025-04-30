package pl.edu.zut.app.parking.parking.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.dto.requests.ParkingCreateRequest;
import pl.edu.zut.app.parking.parking.dto.responses.ParkingDetailsResponse;
import pl.edu.zut.app.parking.parking.services.ParkingService;


@RestController
@RequestMapping("/api/v1/parking/creation")
@RequiredArgsConstructor
public class ParkingCreationController {


    private final ParkingService parkingService;

    @PostMapping
    @PreAuthorize("hasRole('CREATE_PARKING')")
    public ResponseEntity<ParkingDetailsResponse> createParking(@RequestBody ParkingCreateRequest request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(parkingService.createParking(request));
    }
}
