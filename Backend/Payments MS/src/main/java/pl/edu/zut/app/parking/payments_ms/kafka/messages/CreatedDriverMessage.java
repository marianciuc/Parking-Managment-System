package pl.edu.zut.app.parking.payments_ms.kafka.messages;

import java.util.UUID;

/**
 * Represents a message created upon registering a new driver.
 * This record is typically used for transferring information during the
 * process of creating and initializing a new driver in the system.
 *
 * It holds information such as the driver's unique identifier, email, personal
 * details, profile picture URL, email verification status, and locale.
 *
 * Fields:
 * - driverId (UUID): The unique identifier of the created driver.
 * - email (String): The email address of the created driver.
 * - givenName (String): The first name of the created driver.
 * - familyName (String): The last name of the created driver.
 * - pictureUrl (String): The URL to the profile picture of the created driver.
 * - emailVerified (Boolean): Indicates if the driver's email address is verified.
 * - locale (String): The locale or language preference associated with the driver.
 */
public record CreatedDriverMessage(
        UUID driverId,
        String email,
        String givenName,
        String familyName,
        String pictureUrl,
        Boolean emailVerified,
        String locale
) {
}
