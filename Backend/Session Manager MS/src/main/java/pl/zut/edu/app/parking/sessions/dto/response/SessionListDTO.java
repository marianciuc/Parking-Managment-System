package pl.zut.edu.app.parking.sessions.dto.response;

import pl.zut.edu.app.parking.sessions.entities.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionListDTO(
        UUID id,
        UUID parkingId,
        String status,
        String plateNumber,
        String tariffName,
        String accessList,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String paymentStatus
) {
    public static SessionListDTO fromEntity(Session session) {
        return new SessionListDTO(
                session.getId(),
                session.getParkingId(),
                session.getStatus().name(),
                session.getPlateNumber(),
                session.getTariffName(),
                session.getVehicleAccessList(),
                session.getStartTime(),
                session.getEndTime(),
                session.getPaidUntil() == null ? "Not paid" :
                        session.getPaidUntil().plusMinutes(15)
                                .isBefore(LocalDateTime.now()) ? "Paid" : "Not paid"
        );
    }
}
