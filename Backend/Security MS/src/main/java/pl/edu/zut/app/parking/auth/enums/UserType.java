package pl.edu.zut.app.parking.auth.enums;

/**
 * Enum representing different types of users in the parking system.
 * <p>
 * The {@code UserType} enum classifies the roles of users
 * interacting with the parking system. Each user role is associated
 * with specific responsibilities and permissions, helping to define
 * the access control and functionality available to them.
 */
public enum UserType {
    /**
     * Represents customers of the parking system.
     * <p>
     * Examples:
     * - Individuals using the parking facilities for temporary or long-term parking.
     * - People looking for parking spaces.
     */
    DRIVER,
    /**
     * Represents owners of the parking system.
     * <p>
     * Responsibilities:
     * - Managing parking infrastructure, rates, and policies.
     * - Overseeing operational performance and maintenance of the system.
     */
    PARKING_OWNER,
    /**
     * Represents organizational personnel of the parking system.
     * <p>
     * Examples:
     * - Staff responsible for day-to-day operations and customer support.
     * - Personnel handling technical maintenance and troubleshooting.
     */
    ORGANIZATION_PERSONAL,
    /**
     * Represents administrators of the parking system.
     * <p>
     * Responsibilities:
     * - Managing user accounts, permissions, and roles.
     * - Ensuring system security, uptime, and policy compliance.
     */
    ADMIN
}
