package pl.edu.zut.app.parking.payments_ms.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.edu.zut.app.parking.payments_ms.exceptions.*;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

    static {
        EXCEPTION_STATUS_MAP.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(IllegalStateException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS_MAP.put(AccountBalanceAlreadyExistsException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS_MAP.put(AccountBalanceNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(AccountBalanceInvalidArgumentsException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(NullPointerException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(NumberFormatException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(BalanceAccountInvalidOperationException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(ExchangeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(ParkingNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(InsufficientFundsException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(InvalidSessionStatus.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS_MAP.put(TransactionNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(SessionPaymentException.class, HttpStatus.BAD_REQUEST);
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
