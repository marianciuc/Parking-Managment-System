package pl.edu.zut.app.parking.parking.services;

import java.util.List;
import java.util.UUID;
import pl.edu.zut.app.parking.exception.ForbiddenException;
import pl.edu.zut.app.parking.exception.InvalidAuthenticationPrincipalException;
import pl.edu.zut.app.parking.parking.dto.common.GateDto;
import pl.edu.zut.app.parking.parking.exceptions.GateIsOfflineException;
import pl.edu.zut.app.parking.parking.exceptions.GateNotFoundException;

/**
 * Interface for managing gate control operations within a parking management system.
 * Provides methods for opening, closing, registering, and modifying gates, as well as
 * retrieving gate details.
 */
public interface GateControlService {

  /**
   * Opens all gates associated with the given parking lot ID in case of an emergency.
   *
   * @param parkingId the unique identifier of the parking lot whose gates need to be opened
   */
  void emergencyOpenGates(UUID parkingId);

  /**
   * Opens a gate with the given gate ID and changes it mode to manual.
   *
   * @param gateId the unique identifier of the gate to be opened
   * @throws GateIsOfflineException if the gate is not online or cannot be reached
   * @throws GateNotFoundException if the gate with the specified ID does not exist
   */
  void openGate(UUID gateId) throws GateIsOfflineException;

  /**
   * Closes the specified gate by its unique identifier and changes it mode to manual.
   *
   * @param gateId The unique identifier of the gate to be closed.
   * @throws GateIsOfflineException If the gate is offline or unavailable for operation.
   * @throws GateNotFoundException If the gate with the specified identifier is not found.
   */
  void closeGate(UUID gateId) throws GateIsOfflineException;

  /**
   * Retrieves a list of gates associated with a specific parking lot identified by its ID.
   *
   * @param parkingId the unique identifier of the parking lot
   * @return a list of GateDto objects representing the gates of the specified parking lot
   */
  List<GateDto> getGatesByParkingId(UUID parkingId);

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
  GateDto registerGate(GateDto gateDto);

  /**
   * Unregisters a gate by its unique identifier.
   *
   * @param gateId the unique identifier of the gate to be unregistered
   * @throws InvalidAuthenticationPrincipalException if the security context does not contain valid
   *     authentication details
   * @throws GateNotFoundException if the specified gate does not exist
   * @throws ForbiddenException if the current user is not authorized to unregister the gate
   */
  void unregisterGate(UUID gateId);

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
  void changeManualMode(UUID gateId, Boolean manualMode);
}
