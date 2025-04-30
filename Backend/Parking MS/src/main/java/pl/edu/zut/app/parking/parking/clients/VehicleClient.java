package pl.edu.zut.app.parking.parking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.parking.dto.VehicleDto;


@FeignClient(name = "vehicle-service")
public interface VehicleClient {

    @GetMapping("/api/v1/vehicles/find-by-plate")
    ResponseEntity<VehicleDto> findByPlate(@RequestParam String plate);

    @PostMapping(value = "/api/v1/vehicles")
    ResponseEntity<VehicleDto> create(@RequestBody VehicleDto vehicleDto, @RequestParam(name = "assignOwnership",
            defaultValue = "false") Boolean assignOwnership);
}