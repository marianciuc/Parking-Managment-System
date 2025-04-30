package pl.edu.zut.app.parking.cars.handlers;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.edu.zut.app.parking.cars.dto.ExceptionResponse;
import pl.edu.zut.app.parking.cars.exceptions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

    static {
        EXCEPTION_STATUS_MAP.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(ForbiddenException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(InvalidRequestStatusException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(InvalidVehicleDataException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(OwnershipNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(RequestNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(VehicleNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(VehicleAlreadyExistsException.class, HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> createResponseEntity(HttpStatus status, String message) {
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return createResponseEntity(status, ex.getMessage());
    }
}
