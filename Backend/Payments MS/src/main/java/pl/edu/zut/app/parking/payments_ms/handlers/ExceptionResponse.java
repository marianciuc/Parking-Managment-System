package pl.edu.zut.app.parking.payments_ms.handlers;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime timestamp,
        String message
) {}
