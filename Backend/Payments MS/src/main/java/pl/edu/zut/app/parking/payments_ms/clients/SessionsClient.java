package pl.edu.zut.app.parking.payments_ms.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.zut.app.parking.payments_ms.config.FeignClientConfig;
import pl.edu.zut.app.parking.payments_ms.dto.external.SessionDto;

/**
 * A Feign client interface for interacting with the session service. This client is used to
 * communicate with the session-service and perform operations related to session data retrieval.
 *
 * <p>The Feign client is configured using {@code FeignClientConfig} to set up authentication and
 * other required headers in the requests.
 */
@FeignClient(name = "session-service", configuration = FeignClientConfig.class)
public interface SessionsClient {

  /**
   * Retrieves the details of a session by its unique identifier.
   *
   * @param sessionId the unique identifier of the session to retrieve
   * @return a {@link ResponseEntity} containing the details of the session encapsulated in a {@code
   *     SessionDto}
   */
  @GetMapping("/api/v1/sessions/{sessionId}")
  ResponseEntity<SessionDto> getSession(@PathVariable UUID sessionId);
}
