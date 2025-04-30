package pl.edu.zut.app.parking.payments_ms.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SessionDto(
    UUID id,
    long durationInMinutes,
    String status,
    String currency,
    BigDecimal totalAmount,
    String vehiclePlateNumber,
    UUID vehicleId,
    UUID parkingId,
    String vehicleAccessList,
    String tariffName,
    UUID tariffId) {}
