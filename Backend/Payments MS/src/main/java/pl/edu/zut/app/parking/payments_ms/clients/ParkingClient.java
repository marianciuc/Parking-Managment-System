package pl.edu.zut.app.parking.payments_ms.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.edu.zut.app.parking.payments_ms.config.FeignClientConfig;

/**
 * A Feign client interface for interacting with the parking service. This client is used to
 * communicate with the parking-service and perform operations related to parking data retrieval.
 *
 * <p>The Feign client is configured using {@code FeignClientConfig} to set up authentication and
 * other required headers in the requests.
 */
@FeignClient(name = "parking-service", configuration = FeignClientConfig.class)
public interface ParkingClient {

  /**
   * Retrieves the owner ID of the specified parking.
   *
   * @param parkingId the unique identifier of the parking for which the owner ID is being retrieved
   * @return a {@link ResponseEntity} containing the UUID of the parking owner
   */
  @GetMapping("/api/v1/parking/{parkingId}/owner-id")
  ResponseEntity<UUID> getParkingOwenrId(@PathVariable UUID parkingId);
}
