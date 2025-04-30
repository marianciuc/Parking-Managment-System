package pl.edu.zut.app.parking.auth.dto.res;

import java.time.LocalDateTime;

public record ExceptionResponse(
        LocalDateTime timestamp,
        String message
) {
}
