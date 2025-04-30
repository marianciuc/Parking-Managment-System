package pl.zut.edu.app.parking.sessions.dto.filters;

import pl.zut.edu.app.parking.sessions.entities.Session;

import java.util.UUID;

public record SessionFilter(
        UUID id,
        UUID parkingId,
        UUID vehicleId,
        UUID ownerId,
        String plateNumber,
        Session.SessionStatus status
) {
}
