

/**
 * Represents a domain object for a user with relevant personal and system information.
 *
 * This interface is used to define the structure of a user object containing
 * various details such as identification, personal details, system usage information,
 * employment data, and status indicators.
 *
 * Properties:
 * - id: Unique identifier for the user.
 * - firstname: First name of the user.
 * - lastname: Last name of the user.
 * - profileImageUrl: URL pointing to the user's profile image.
 * - phoneNumber: Contact phone number of the user.
 * - systemUserId: Unique identifier for the user in the system.
 * - hireDate: Date when the user was hired or started in the organization.
 * - terminationDate: Date when the user's employment was terminated, if applicable.
 * - dateOfBirth: Date of birth of the user.
 * - isActiveAdministrator: Indicates whether the user is an active administrator.
 * - creationDate: Date when the user was created in the system.
 * - modificationDate: Most recent date when the user's data was modified.
 */
export interface AdministratorDomain {
    id: string;
    firstname: string;
    lastname: string;
    profileImageUrl: string;
    phoneNumber: string;
    systemUserId: string;
    hireDate: Date;
    terminationDate: Date;
    dateOfBirth: Date;
    isActiveAdministrator: boolean;
    creationDate: Date;
    modificationDate: Date;
}