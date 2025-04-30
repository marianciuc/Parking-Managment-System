package pl.edu.zut.app.parking.cars.dto;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime timestamp,
        String message
) {
}
