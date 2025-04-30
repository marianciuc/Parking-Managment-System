package pl.zut.edu.app.parking.sessions.services;

import org.springframework.data.domain.Page;
import pl.zut.edu.app.parking.sessions.dto.Payment;
import pl.zut.edu.app.parking.sessions.dto.PrepareEndSessionDto;
import pl.zut.edu.app.parking.sessions.dto.SessionDto;
import pl.zut.edu.app.parking.sessions.dto.filters.SessionFilter;
import pl.zut.edu.app.parking.sessions.dto.response.SessionListDTO;
import pl.zut.edu.app.parking.sessions.kafka.messages.SessionPaymentMessage;

import java.util.UUID;

/**
 * This interface provides a set of operations to manage parking sessions.
 * It includes methods for creating, starting, canceling, ending, retrieving,
 * and handling payments for sessions.
 */
public interface SessionService {

    /**
     * Prepares a parking session for the given vehicle identified by its license plate.
     * This method generates a unique identifier for the parking session and initializes
     * it for further processing such as activation or management.
     *
     * @param plate the license plate of the vehicle for which the parking session is being prepared
     * @return a {@code UUID} representing the unique identifier for the prepared parking session
     */
    UUID prepareSession(String plate);

    /**
     * Starts a parking session identified by the given session ID.
     * This method initializes and activates a parking session,
     * allowing tracking and management of the session's progress.
     *
     * @param sessionId the unique identifier of the parking session to be started
     */
    void startSession(UUID sessionId);

    /**
     * Cancels an active parking session identified by the given session ID.
     * This method is used to terminate and mark a session as canceled,
     * ensuring that any ongoing processes or statuses related to the session
     * are updated accordingly.
     *
     * @param sessionId the unique identifier of the parking session to be canceled
     */
    void cancelSession(UUID sessionId);

    /**
     * Ends the parking session associated with the provided license plate.
     * This method finalizes the active session for the specified vehicle,
     * ensuring that all related processes, such as payment or status updates, are completed.
     *
     * @param plate the license plate of the vehicle for which the active parking session is to be ended
     */
    void endSession(String plate);

    /**
     * Retrieves a parking session based on the provided unique session identifier.
     *
     * @param sessionId the unique identifier of the parking session to be retrieved
     * @return an instance of {@code SessionDto} representing the details of the parking session
     * @throws pl.zut.edu.app.parking.sessions.exceptions.SessionNotFoundException if the session is not founded
     */
    SessionDto findSession(UUID sessionId);

    /**
     * Retrieves the active parking session associated with the specified license plate.
     *
     * @param plate the license plate of the vehicle for which the active session is to be retrieved
     * @return an instance of {@code SessionDto} representing the details of the active parking session,
     *         or null if no active session exists for the specified license plate
     * @throws pl.zut.edu.app.parking.sessions.exceptions.SessionNotFoundException if the session is not founded
     */
    SessionDto findActiveSessionByPlate(String plate);

    /**
     * Prepares the necessary details for ending a parking session associated with the provided license plate.
     * This method calculates payment details, checks the paid status, and retrieves the session's unique identifier.
     *
     * @param plate the license plate of the vehicle for which the parking session is to be ended
     * @return an instance of {@code PrepareEndSessionDto} containing information about the payment status,
     *         payment details, and the session's unique identifier
     */
    PrepareEndSessionDto prepareEndSession(String plate);

    /**
     * Cancels a prepared end session process for a given vehicle identified by its license plate.
     * This method is used when an end session preparation has been initiated but needs to be revoked.
     *
     * @param plate the license plate of the vehicle for which the prepared end session should be canceled
     */
    void cancelPreparedEndSession(String plate);

    /**
     * Retrieves a paginated list of parking sessions based on the provided filter and sorting options.
     *
     * @param page the page number to retrieve, starting from 0
     * @param size the number of sessions per page
     * @param sort the field by which to sort the results
     * @param direction the sorting direction, either "asc" for ascending or "desc" for descending
     * @param filter the filter criteria to apply when retrieving sessions
     * @return a paginated {@code Page} of {@code SessionListDTO} objects representing the matching sessions
     */
    Page<SessionListDTO> findSessions(Integer page, Integer size, String sort, String direction, SessionFilter filter);

    /**
     * Adds a payment to the relevant parking session. This method processes
     * the payment details provided in the message and associates the payment
     * with the corresponding parking session.
     *
     * @param message the message containing information about the payment,
     *                including the session ID, payment ID, and the amount of
     *                paid time in minutes
     */
    void addPayment(SessionPaymentMessage message);

    /**
     * Calculates the payment details for a completed parking session.
     * This includes the duration of the session, the total amount to be paid, and the currency used.
     *
     * @param sessionId the unique identifier of the parking session for which the payment is to be calculated
     * @return an instance of {@code Payment} containing details such as session duration,
     *         total payment amount, and currency
     */
    Payment calculatePayment(UUID sessionId);
}
