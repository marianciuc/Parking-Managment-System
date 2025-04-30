package pl.zut.edu.app.parking.sessions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zut.edu.app.parking.sessions.config.FeignClientConfig;
import pl.zut.edu.app.parking.sessions.dto.VehicleDto;

@FeignClient(name = "vehicle-service", configuration = FeignClientConfig.class)
public interface VehicleClient {

    @GetMapping("/api/v1/vehicles/find-by-plate")
    ResponseEntity<VehicleDto> findByPlate(@RequestParam String plate);

    @PostMapping(value = "/api/v1/vehicles")
    ResponseEntity<VehicleDto> create(@RequestBody VehicleDto vehicleDto, @RequestParam(name = "assignOwnership", defaultValue = "false") Boolean assignOwnership);
}
