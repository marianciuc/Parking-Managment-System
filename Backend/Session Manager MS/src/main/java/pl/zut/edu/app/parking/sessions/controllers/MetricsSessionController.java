package pl.zut.edu.app.parking.sessions.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zut.edu.app.parking.sessions.dto.response.MetricsDto;
import pl.zut.edu.app.parking.sessions.dto.response.PeakHours;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehicleStatsDto;
import pl.zut.edu.app.parking.sessions.dto.response.TopVehiclesInPeakHoursDto;
import pl.zut.edu.app.parking.sessions.services.SessionMetricsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions/metrics")
@RequiredArgsConstructor
public class MetricsSessionController {
    private final SessionMetricsService sessionMetricsService;

    @GetMapping("/{parkingId}")
    public ResponseEntity<MetricsDto> getMetrics(@PathVariable("parkingId") UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getMetricsForParking(parkingId));
    }

    @GetMapping("/{parkingId}/active-sessions")
    public ResponseEntity<Integer> getActiveSessions(@PathVariable("parkingId") UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getAmountOfActiveSessions(parkingId));
    }

    @GetMapping("/{parkingId}/average-time")
    public ResponseEntity<Double> getAverageTime(@PathVariable("parkingId") UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.calculateAverageTimeOfSessions(parkingId));
    }

    @GetMapping("/{parkingId}/finished-sessions")
    public ResponseEntity<Integer> getAmountOfFinishedSessions(@PathVariable("parkingId") UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getAmountOfFinishedSessions(parkingId));
    }

    @GetMapping("/{parkingId}/propositions")
    public ResponseEntity<List<TopVehicleStatsDto>> getPersonalTariffProposition(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getPersonalTariffProposition(parkingId));
    }

    @GetMapping("/{parkingId}/peak-hours")
    public ResponseEntity<List<PeakHours>> getPeakHours(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getPeakHours(parkingId));
    }

    @GetMapping("/{parkingId}/peak-hours/top-vehicles")
    public ResponseEntity<List<TopVehiclesInPeakHoursDto>> getPeakHoursTopVehicles(@PathVariable UUID parkingId) {
        return ResponseEntity.ok(sessionMetricsService.getTopVehiclesInPeakHours(parkingId));
    }
}
