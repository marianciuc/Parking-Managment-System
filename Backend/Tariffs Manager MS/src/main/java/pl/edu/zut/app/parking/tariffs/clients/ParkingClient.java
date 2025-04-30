package pl.edu.zut.app.parking.tariffs.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.zut.app.parking.tariffs.config.FeignClientConfig;
import pl.edu.zut.app.parking.tariffs.dto.external.responces.ParkingResDto;

import java.util.UUID;

@FeignClient(name = "parking-service", configuration = FeignClientConfig.class)
public interface ParkingClient {

    @GetMapping("/api/v1/parking/{parkingId}")
    ResponseEntity<ParkingResDto> getParkingById(@PathVariable UUID parkingId);
}
