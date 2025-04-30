package pl.edu.zut.app.parking.cars.controllers;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.cars.dto.VehicleDto;
import pl.edu.zut.app.parking.cars.dto.VehicleSearchRequest;
import pl.edu.zut.app.parking.cars.entities.Ownership;
import pl.edu.zut.app.parking.cars.services.VehicleService;

@Slf4j
@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

  private final VehicleService vehicleService;

  @GetMapping("/search")
  public ResponseEntity<Page<VehicleDto>> find(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) UUID id,
      @RequestParam(required = false) String plateNumber,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String model,
      @RequestParam(required = false) UUID ownerId,
      @RequestParam(required = false) String color,
      @RequestParam(required = false) Ownership.OwnerType ownerType,
      @RequestParam(required = false) Integer yearOfProduction) {
    return ResponseEntity.ok(
        vehicleService.find(
            page,
            size,
            new VehicleSearchRequest(
                id, plateNumber, brand, model, color, ownerId, ownerType, yearOfProduction)));
  }

  @GetMapping("/find-by-plate")
  public ResponseEntity<VehicleDto> findByPlate(@RequestParam String plate) {
    return ResponseEntity.ok(vehicleService.findByPlate(plate));
  }

  @GetMapping("/{vehicleId}")
  public ResponseEntity<VehicleDto> findById(@PathVariable UUID vehicleId) {
    return ResponseEntity.ok(vehicleService.findById(vehicleId));
  }

  @PutMapping("/{vehicleId}")
  public ResponseEntity<VehicleDto> update(
      @PathVariable UUID vehicleId, @RequestBody VehicleDto vehicleDto) {
    return ResponseEntity.ok(vehicleService.update(vehicleId, vehicleDto));
  }

  @PostMapping
  public ResponseEntity<VehicleDto> create(
      @RequestBody VehicleDto vehicleDto,
      @RequestParam(name = "assignOwnership", defaultValue = "false") Boolean assignOwnership) {
    log.info("Request to create vehicle: {}, assignOwnership: {}", vehicleDto, assignOwnership);
    return ResponseEntity.ok(vehicleService.create(vehicleDto, assignOwnership));
  }
}
