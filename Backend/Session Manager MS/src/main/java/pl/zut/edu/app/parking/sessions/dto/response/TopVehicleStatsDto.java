package pl.zut.edu.app.parking.sessions.dto.response;

import java.util.UUID;

public record TopVehicleStatsDto(
        UUID vehicleId,
        String plateNumber,
        double avgSessionsPerMonth
        ) {
}
