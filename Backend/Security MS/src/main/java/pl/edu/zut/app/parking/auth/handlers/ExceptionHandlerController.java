package pl.edu.zut.app.parking.auth.handlers;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.edu.zut.app.parking.auth.dto.res.ExceptionResponse;
import pl.edu.zut.app.parking.auth.exceptions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController {
    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

    static {
        EXCEPTION_STATUS_MAP.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(AccountExpiredException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(AccountLockedException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(AuthorizationException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(EmailAlreadyExistsException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS_MAP.put(UsernameAlreadyExistsException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS_MAP.put(ExpiredTokenException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(InvalidTokenException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(TokenEncryptionException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(TokenSigningException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS_MAP.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS_MAP.put(ForbiddenUserRegistrationException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(ForbiddenException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(InvalidCredentialsException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(InvalidApiKeyException.class, HttpStatus.FORBIDDEN);
        EXCEPTION_STATUS_MAP.put(OAuthTokenException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(TokenValidationException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS_MAP.put(AccountNotSupportedLoginByCredentialsException.class, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Object> createResponseEntity(HttpStatus status, String message) {
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        org.springframework.http.HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        return createResponseEntity(status, ex.getMessage());
    }
}
