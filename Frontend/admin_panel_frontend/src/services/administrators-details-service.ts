import {AdministratorDomain, Page} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const ADMINISTRATOR_DETAILS_ENDPOINTS = {
    CHECK_PERMISSIONS: () => `/api/v1/administrators/permissions`,
    ADMINISTRATOR_DETAILS: (id: string) => {
        if (id === "me") {
            return `/api/v1/administrators/details`
        }
        return `/api/v1/administrators/${id}/details`
    },
    TERMINATE_ADMINISTRATOR: (id: string) => `/api/v1/administrators/${id}/terminate`,
    SEARCH_ADMINISTRATORS: () => `/api/v1/administrators/search`,
};


/**
 * AdministratorsDetailsService provides a set of static methods to interact with
 * API endpoints for managing administrator details and permissions.
 */
export default class AdministratorsDetailsService {

    /**
     * Checks the permissions for a given administrator based on their administrator ID.
     *
     * @param {string} administratorId - The unique identifier of the administrator whose permissions are to be checked.
     * @return {Promise<AxiosResponse<void>>} A promise that resolves to the Axios response containing the result of the permission check.
     */
    public static async checkPermissions(): Promise<AxiosResponse<void>> {
        return apiClient.get(ADMINISTRATOR_DETAILS_ENDPOINTS.CHECK_PERMISSIONS());
    }

    /**
     * Fetches the details of an administrator by their unique identifier.
     *
     * @param {string} administratorId - The unique identifier of the administrator whose details are to be retrieved.
     * @return {Promise<AxiosResponse<AdministratorDomain>>} A promise resolving to the Axios response containing the administrator details.
     */
    public static async fetchAdministratorsDetails(administratorId: string): Promise<AxiosResponse<AdministratorDomain>> {
        return apiClient.get(ADMINISTRATOR_DETAILS_ENDPOINTS.ADMINISTRATOR_DETAILS(administratorId));
    }

    /**
     * Updates the details of an administrator based on the given administrator ID.
     *
     * @param {string} administratorId - The unique identifier of the administrator whose details need to be updated.
     * @return {Promise<AxiosResponse<AdministratorDomain>>} A promise that resolves to the server's response containing the updated administrator details.
     */
    public static async updateAdministratorDetails(administratorId: string): Promise<AxiosResponse<AdministratorDomain>> {
        return apiClient.put(ADMINISTRATOR_DETAILS_ENDPOINTS.ADMINISTRATOR_DETAILS(administratorId));
    }

    /**
     * Terminates an administrator account by calling the respective endpoint.
     *
     * @param {string} administratorId - The unique identifier of the administrator to be terminated.
     * @return {Promise<AxiosResponse<void>>} A promise that resolves to the Axios response indicating the result of the termination operation.
     */
    public static async terminateAdministrator(administratorId: string): Promise<AxiosResponse<void>> {
        return apiClient.delete(ADMINISTRATOR_DETAILS_ENDPOINTS.TERMINATE_ADMINISTRATOR(administratorId));
    }

    /**
     * Searches for administrators based on the provided filter criteria.
     *
     * @param {Object} filter - The filtering options for the search.
     * @param {string} filter.sort - The field to sort the results by.
     * @param {string} filter.direction - The direction to sort the results (e.g., ascending or descending).
     * @param {number} filter.page - The page number for paginated results.
     * @param {number} filter.size - The number of results per page.
     * @param {string} filter.firstname - The first name of the administrator to search for.
     * @param {string} filter.lastname - The last name of the administrator to search for.
     * @return {Promise<AxiosResponse<Page<AdministratorDomain>>>} A promise resolving to the API response containing a paginated list of administrators.
     */
    public static async searchAdministrators(filter: {
        sort: string,
        direction: string,
        page: number,
        size: number,
        firstname: string,
        lastname: string
    }): Promise<AxiosResponse<Page<AdministratorDomain>>> {
        return apiClient.get(ADMINISTRATOR_DETAILS_ENDPOINTS.SEARCH_ADMINISTRATORS(), {params: filter})
    }

    /**
     * Fetches the details of the authenticated administrator.
     *
     * This method retrieves the administrator details for the currently authenticated user
     * by making a GET request to the appropriate endpoint.
     *
     * @return {Promise<AxiosResponse<AdministratorDomain>>} A promise resolving to the Axios response containing the administrator's details.
     */
    public static async fetchAuthenticatedAdministratorDetails(): Promise<AxiosResponse<AdministratorDomain>> {
        return apiClient.get(ADMINISTRATOR_DETAILS_ENDPOINTS.ADMINISTRATOR_DETAILS("me"));
    }
}