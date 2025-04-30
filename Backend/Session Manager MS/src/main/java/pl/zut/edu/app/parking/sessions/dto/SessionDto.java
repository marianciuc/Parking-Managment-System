package pl.zut.edu.app.parking.sessions.dto;

import pl.zut.edu.app.parking.sessions.entities.Session;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record SessionDto(
        UUID id,
        long durationInMinutes,
        String status,
        BigDecimal totalPaid,
        String currency,
        BigDecimal totalAmount,
        String vehiclePlateNumber,
        UUID vehicleId,
        UUID parkingId,
        String vehicleAccessList,
        Payment payment,
        String tariffName,
        UUID tariffId
) {
    public static SessionDto from(Session session, String currency, BigDecimal totalPaid, Payment payment,
                                  BigDecimal totalAmount, long durationInMinutes) {
        return new SessionDto(
                session.getId(),
                durationInMinutes,
                session.getStatus() != null ? session.getStatus().name() : null,
                totalPaid,
                currency,
                totalAmount,
                session.getPlateNumber(),
                session.getVehicleId(),
                session.getParkingId(),
                session.getVehicleAccessList(),
                payment,
                session.getTariffName(),
                session.getTariffId()
        );
    }
}
