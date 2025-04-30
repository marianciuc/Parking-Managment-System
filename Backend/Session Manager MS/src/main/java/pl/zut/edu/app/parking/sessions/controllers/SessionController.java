package pl.zut.edu.app.parking.sessions.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.zut.edu.app.parking.sessions.dto.PrepareEndSessionDto;
import pl.zut.edu.app.parking.sessions.dto.SessionDto;
import pl.zut.edu.app.parking.sessions.dto.filters.SessionFilter;
import pl.zut.edu.app.parking.sessions.dto.response.SessionListDTO;
import pl.zut.edu.app.parking.sessions.entities.Session;
import pl.zut.edu.app.parking.sessions.services.SessionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/prepare")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<String> prepareSession(@RequestParam String plate) {
        return ResponseEntity.ok(sessionService.prepareSession(plate).toString());
    }

    @PostMapping("/{sessionId}/start")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<Void> startSession(@PathVariable UUID sessionId) {
        sessionService.startSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/cancel")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<Void> cancelSession(@PathVariable UUID sessionId) {
        sessionService.cancelSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/prepare-end")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<PrepareEndSessionDto> prepareEndSession(@RequestParam(name = "plate") String plate) {
        return ResponseEntity.ok(sessionService.prepareEndSession(plate));
    }

    @PostMapping("/cancel-prepared-end")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<Void> cancelPreparedEndSession(@RequestParam(name = "plate") String plate) {
        sessionService.cancelPreparedEndSession(plate);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/end")
    @PreAuthorize("hasRole('PARKING')")
    public ResponseEntity<Void> endSession(@RequestParam(name = "plate") String plate) {
        sessionService.endSession(plate);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionDto> getSession(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(sessionService.findSession(sessionId));
    }

    @GetMapping("/find-by-plate")
    public ResponseEntity<SessionDto> findByPlate(@RequestParam String plate) {
        return ResponseEntity.ok(sessionService.findActiveSessionByPlate(plate));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SessionListDTO>> find(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) String direction,
                                                     @RequestParam(required = false) UUID parkingId,
                                                     @RequestParam(required = false) Session.SessionStatus status,
                                                     @RequestParam(required = false) UUID vehicleId,
                                                     @RequestParam(required = false) String plateNumber,
                                                     @RequestParam(required = false) UUID ownerId,
                                                     @RequestParam(required = false) UUID id) {
        SessionFilter sessionFilter = new SessionFilter(
                id,
                parkingId,
                vehicleId,
                ownerId,
                plateNumber,
                status
        );
        return ResponseEntity.ok(sessionService.findSessions(page, size, sort, direction, sessionFilter));
    }
}
