package pl.zut.edu.app.parking.sessions.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zut.edu.app.parking.sessions.config.FeignClientConfig;
import pl.zut.edu.app.parking.sessions.dto.TariffDto;

import java.util.UUID;

@FeignClient(name = "parking-service", configuration = FeignClientConfig .class)
public interface ParkingClient {

    @GetMapping("/api/v1/parking/{parkingId}/active-tariff")
    ResponseEntity<TariffDto> getActiveTariff(@PathVariable UUID parkingId,
                                              @RequestParam(name = "vehicleId") UUID vehicleId,
                                              @RequestParam(name = "minutes") long minutes, @RequestParam String currency);

    @GetMapping("/api/v1/parking/{parkingId}/access/{vehicleId}")
    ResponseEntity<Boolean> isVehicleAllowed(@PathVariable UUID parkingId, @PathVariable UUID vehicleId);

    @GetMapping("/api/v1/parking/{parkingId}/access/whitelist/{vehicleId}")
    ResponseEntity<Boolean> isInWhitelist(@PathVariable UUID parkingId,@PathVariable UUID vehicleId);

    @GetMapping("/api/v1/parking/{parkingId}/default-currency")
    ResponseEntity<String> getParkingDefaultCurrency(@PathVariable UUID parkingId);
}
