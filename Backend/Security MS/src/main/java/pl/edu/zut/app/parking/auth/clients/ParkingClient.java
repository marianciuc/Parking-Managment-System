package pl.edu.zut.app.parking.auth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "parking-service")
public interface ParkingClient {

    @GetMapping("/api/v1/parking/{parkingId}")
    ResponseEntity<Object> isParkingAvailable(@PathVariable UUID parkingId);
}
