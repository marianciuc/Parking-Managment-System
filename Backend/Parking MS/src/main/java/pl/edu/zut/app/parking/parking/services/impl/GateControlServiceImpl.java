package pl.edu.zut.app.parking.parking.services.impl;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.edu.zut.app.parking.exception.ForbiddenException;
import pl.edu.zut.app.parking.exception.InvalidAuthenticationPrincipalException;
import pl.edu.zut.app.parking.parking.dto.common.GateDto;
import pl.edu.zut.app.parking.parking.entities.Gate;
import pl.edu.zut.app.parking.parking.entities.Parking;
import pl.edu.zut.app.parking.parking.exceptions.GateIsOfflineException;
import pl.edu.zut.app.parking.parking.exceptions.GateNotFoundException;
import pl.edu.zut.app.parking.parking.repositories.GateRepository;
import pl.edu.zut.app.parking.parking.services.GateControlService;
import pl.edu.zut.app.parking.utils.SecurityContextUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class GateControlServiceImpl implements GateControlService {
  private static final String API_GATE_OPEN = "/api/v1/gate/open";
  private static final String API_GATE_CLOSE = "/api/v1/gate/close";
  private static final String API_GATE_STATUS = "/api/v1/gate/status";

  private final EntityManager entityManager;
  private final GateRepository gateRepository;
  private final RestTemplate restTemplate = new RestTemplate();

  /**
   * Opens all gates associated with the given parking lot ID in case of an emergency.
   *
   * @param parkingId the unique identifier of the parking lot whose gates need to be opened
   */
  @Override
  public void emergencyOpenGates(UUID parkingId) {
    gateRepository.findAllByParking_id(parkingId).forEach(gate -> openGate(gate.getId()));
  }

  /**
   * Opens a gate with the given gate ID.
   *
   * @param gateId the unique identifier of the gate to be opened
   * @throws GateIsOfflineException if the gate is not online or cannot be reached
   * @throws GateNotFoundException if the gate with the specified ID does not exist
   */
  @Override
  public void openGate(UUID gateId) throws GateIsOfflineException, GateNotFoundException {
    executeGateOperation(gateId, API_GATE_OPEN, Gate.GateStatus.OPEN, true);
  }

  /**
   * Closes the specified gate by its unique identifier.
   *
   * @param gateId The unique identifier of the gate to be closed.
   * @throws GateIsOfflineException If the gate is offline or unavailable for operation.
   * @throws GateNotFoundException If the gate with the specified identifier is not found.
   */
  @Override
  public void closeGate(UUID gateId) throws GateIsOfflineException, GateNotFoundException {
    executeGateOperation(gateId, API_GATE_CLOSE, Gate.GateStatus.CLOSED, true);
  }

  private void executeGateOperation(
      UUID gateId, String apiEndpoint, Gate.GateStatus status, boolean manualMode)
      throws GateIsOfflineException, GateNotFoundException {
    Gate gate = findGateById(gateId);
    logGateOperation(status, gateId, gate);

    try {
      sendRestRequest(gate, apiEndpoint);
      gate.setStatus(status);
      gateRepository.save(gate);
      changeManualMode(gateId, manualMode);
    } catch (RestClientException e) {
      handleRestClientError(gate, e);
    }
  }

  private Gate findGateById(UUID gateId) throws GateNotFoundException {
    return gateRepository
        .findById(gateId)
        .orElseThrow(() -> new GateNotFoundException("Gate not found"));
  }

  private void sendRestRequest(Gate gate, String endpoint) {
    restTemplate.postForEntity(gate.getHost() + endpoint, null, Void.class);
  }

  private void logGateOperation(Gate.GateStatus status, UUID gateId, Gate gate) {
    log.info(
        "Performing operation: {}, Gate ID: {}, Host: {}, Port: {}",
        status,
        gateId,
        gate.getHost(),
        gate.getPort());
  }

  private void handleRestClientError(Gate gate, RestClientException e) {
    log.error("Error performing operation on gate, deleting gate", e);
    gateRepository.deleteById(gate.getId());
    throw new GateIsOfflineException("Gate is offline");
  }

  /**
   * Retrieves a list of gates associated with a specific parking lot identified by its ID.
   *
   * @param parkingId the unique identifier of the parking lot
   * @return a list of GateDto objects representing the gates of the specified parking lot
   */
  @Override
  public List<GateDto> getGatesByParkingId(UUID parkingId) {
    List<Gate> gates = gateRepository.findAllByParking_id(parkingId);
    return gates.stream()
        .map(gate -> updateGateStatus(gate))
        .filter(java.util.Objects::nonNull)
        .toList();
  }

  private GateDto updateGateStatus(Gate gate) {
    try {
      GateDto gateDto = checkStatus(gate);
      return update(gateDto, gate);
    } catch (RestClientException e) {
      handleRestClientError(gate, e);
      return null;
    }
  }

  private GateDto checkStatus(Gate gate) {
    return restTemplate.getForEntity(gate.getHost() + API_GATE_STATUS, GateDto.class).getBody();
  }

  private GateDto update(GateDto gateDto, Gate gate) {
    gate.setStatus(gateDto.status());
    gate.setManualMode(gateDto.isManualMode());
    return GateDto.fromEntity(gateRepository.save(gate));
  }

  /**
   * Registers a new gate in the system based on the provided gate details and the parking context
   * obtained from the security context.
   *
   * @param gateDto the data transfer object containing details of the gate to be registered,
   *     including its type, host, port, and manual mode status.
   * @return a GateDto object representing the successfully registered gate.
   * @throws InvalidAuthenticationPrincipalException if the security context does not contain valid
   *     authentication information or the parking ID cannot be extracted.
   */
  @Override
  public GateDto registerGate(GateDto gateDto) throws InvalidAuthenticationPrincipalException {
    UUID parkingId = SecurityContextUtil.extractParkingIdFromSecurityContext();
    Parking parking = entityManager.getReference(Parking.class, parkingId);
    Gate gate =
        Gate.builder()
            .isManualMode(gateDto.isManualMode())
            .name(
                gateDto.name() != null
                    ? gateDto.name()
                    : "Gate " + gateRepository.findAllByParking_id(parkingId).toArray().length + 1)
            .host(gateDto.host())
            .port(gateDto.port())
            .type(gateDto.type())
            .status(gateDto.status())
            .parking(parking)
            .build();

    return GateDto.fromEntity(gateRepository.save(gate));
  }

  /**
   * Unregisters a gate by its unique identifier.
   *
   * @param gateId the unique identifier of the gate to be unregistered
   * @throws InvalidAuthenticationPrincipalException if the security context does not contain valid
   *     authentication details
   * @throws GateNotFoundException if the specified gate does not exist
   * @throws ForbiddenException if the current user is not authorized to unregister the gate
   */
  @Override
  public void unregisterGate(UUID gateId)
      throws InvalidAuthenticationPrincipalException, GateNotFoundException, ForbiddenException {
    UUID parkingId = SecurityContextUtil.extractParkingIdFromSecurityContext();
    Gate gate = findGateById(gateId);

    if (!parkingId.equals(gate.getParking().getId())) {
      throw new ForbiddenException(
          "You are not authorized to delete this gate. Only the parking owner can do this.");
    }

    gateRepository.delete(gate);
  }

  /**
   * Changes the manual mode for a specified gate. Updates the gate to reflect the new mode and
   * persists the change. Sends a REST request to notify the external system of the mode change. If
   * the REST request fails, the error is handled appropriately.
   *
   * @param gateId the unique identifier of the gate
   * @param manualMode the new manual mode to be set, where true enables manual mode and false
   *     disables it
   * @throws GateNotFoundException if no gate is found with the specified gateId
   */
  @Override
  public void changeManualMode(UUID gateId, Boolean manualMode) throws GateNotFoundException {
    Gate gate = findGateById(gateId);
    gate.setManualMode(manualMode);

    try {
      sendRestRequest(gate, "/api/v1/gate?mode=" + manualMode);
      gateRepository.save(gate);
    } catch (RestClientException e) {
      handleRestClientError(gate, e);
    }
  }
}
