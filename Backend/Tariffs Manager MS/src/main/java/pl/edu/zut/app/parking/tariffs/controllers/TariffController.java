package pl.edu.zut.app.parking.tariffs.controllers;


import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.edu.zut.app.parking.tariffs.dto.TariffDto;
import pl.edu.zut.app.parking.tariffs.dto.UpdateTariffRequest;
import pl.edu.zut.app.parking.tariffs.services.TariffService;

@RestController
@RequestMapping("/api/v1/tariffs/parking/{parkingId}")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @PostMapping
    public ResponseEntity<TariffDto> createTariff(@PathVariable UUID parkingId,
                                                  @Validated @RequestBody TariffDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tariffService.create(request, parkingId));
    }

    @GetMapping
    public ResponseEntity<List<TariffDto>> getTariffsByParking(@PathVariable UUID parkingId, @RequestParam(required =
            false) Long minutes, @RequestParam(required = false) String currency) {
        return ResponseEntity.ok(tariffService.findAllByTime(parkingId, minutes, currency));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TariffDto>> getTariffsByParkingId(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(tariffService.findAllByParkingId(parkingId));
    }

    @PutMapping("/{tariffId}")
    public ResponseEntity<TariffDto> updateTariff(@PathVariable UUID tariffId,
                                                  @Valid @RequestBody UpdateTariffRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(tariffService.update(tariffId, request));
    }

    @DeleteMapping("/{tariffId}")
    public ResponseEntity<Void> deleteTariff(@PathVariable UUID tariffId) {
        tariffService.delete(tariffId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{tariffId}")
    public ResponseEntity<TariffDto> getTariff(@PathVariable UUID tariffId,
                                               @RequestParam(required = false) String currency) {
        return ResponseEntity.ok(tariffService.find(tariffId, currency));
    }
}
