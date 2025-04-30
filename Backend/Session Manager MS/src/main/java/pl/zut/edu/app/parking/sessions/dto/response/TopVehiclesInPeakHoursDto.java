package pl.zut.edu.app.parking.sessions.dto.response;

import java.util.UUID;

public record TopVehiclesInPeakHoursDto(
        UUID vehicleId,
        String plateNumber,
        long sessionCount
) {
}
