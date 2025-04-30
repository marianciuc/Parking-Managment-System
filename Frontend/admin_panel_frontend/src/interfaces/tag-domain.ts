/**
 * Represents a domain of tags used for parking lot categorization.
 *
 * @interface TagDomain
 *
 * @property {string} id - Unique identifier for the tag domain.
 * @property {string} name - Name of the tag domain.
 * @property {string} description - Detailed description of the tag domain.
 * @property {Date} createdAt - Date and time when the tag domain was created.
 * @property {Date} updatedAt - Date and time when the tag domain was last updated.
 */
export interface TagDomain {
    id: string;
    name: string;
    description: string;
    createdAt: Date;
    updatedAt: Date;
}
