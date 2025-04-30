package pl.edu.zut.app.parking.reviews.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.zut.app.parking.reviews.config.FeignClientConfig;

import java.util.UUID;

@FeignClient(name = "parking-service", configuration = FeignClientConfig.class)
public interface ParkingClient {

    @GetMapping("/api/v1/parking/{parkingId}/exists")
    ResponseEntity<Boolean> parkingExists(@PathVariable UUID parkingId);

}
