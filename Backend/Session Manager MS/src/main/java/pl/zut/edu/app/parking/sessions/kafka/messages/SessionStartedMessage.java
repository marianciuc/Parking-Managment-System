package pl.zut.edu.app.parking.sessions.kafka.messages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a message detailing information about the start of a parking session. This message can
 * include details such as the formatted address where the session started, the plate number of the
 * vehicle, the timestamp of when the session began, and the user identifier.
 *
 * @param formattedAddress formatted to City, Street, building number
 * @param plateNumber a plate number of associated vihicle
 * @param timeStarted the timestamp of when the session began
 * @param userId the id of the owner
 */
public record SessionStartedMessage(
    String formattedAddress, String plateNumber, LocalDateTime timeStarted, UUID userId, UUID parkingId, String parkingName)
    implements Serializable {}
