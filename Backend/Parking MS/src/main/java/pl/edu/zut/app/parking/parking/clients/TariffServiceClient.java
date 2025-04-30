package pl.edu.zut.app.parking.parking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.zut.app.parking.parking.config.FeignClientConfig;
import pl.edu.zut.app.parking.parking.dto.TariffDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "tariffs-service", configuration = FeignClientConfig .class)
public interface TariffServiceClient {

    @GetMapping("/api/v1/tariffs/parking/{parkingId}")
    ResponseEntity<List<TariffDto>> getTariff(@PathVariable UUID parkingId, @RequestParam long minutes,
                                              @RequestParam String currency);

    @GetMapping("/api/v1/tariffs/parking/{parkingId}/{tariffId}")
    ResponseEntity<TariffDto> getTariffById(@PathVariable UUID tariffId, @PathVariable UUID parkingId,
                                            @RequestParam String currency);
}
