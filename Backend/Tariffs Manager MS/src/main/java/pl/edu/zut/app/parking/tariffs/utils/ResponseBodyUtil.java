package pl.edu.zut.app.parking.tariffs.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import pl.edu.zut.app.parking.tariffs.exceptions.ExternalServiceException;

/**
 * Utility class for handling and extracting the body from ResponseEntity objects. It ensures proper error handling
 * when interacting with external services.
 */
@Slf4j
public class ResponseBodyUtil {

    /**
     * Extracts the response body from the ResponseEntity or throws an exception if the response indicates an error
     * or the body is null.
     *
     * @param <T> The type of the response body.
     * @param response The ResponseEntity containing the response from an external service.
     * @param errorMessage A custom error message to include in the exception if the response is invalid.
     * @return The body of the response if no error is detected and the body is non-null.
     * @throws ExternalServiceException If the response indicates an error or the body is null.
     */
    public static <T> T getResponseBodyOrThrow(ResponseEntity<T> response, String errorMessage) {
        if (response.getStatusCode().isError() || response.getBody() == null) {
            log.error("Error calling external service: status={}, body={}",
                    response.getStatusCode(), response.getBody());
            throw new ExternalServiceException(errorMessage +
                    " Status: " + response.getStatusCode() +
                    ", Body: " + response.getBody());
        }
        return response.getBody();
    }

}
