import {APIKey, APIKeyListElement, Page} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const PARKING_ENDPOINTS = {
    SEARCH_API_KEY: () => `/api/v1/api-keys/search`,
    GENERATE: (parkingId: string) => `/api/v1/api-keys/generate/${parkingId}`,
    REVOKE: (parkingId: string) => `/api/v1/api-keys/revoke/${parkingId}`,
    FETCH_API_KEY: (id: string) => `/api/v1/api-keys/details/${id}`,
};


/**
 * A service for managing API keys in a parking management system.
 */
export default class APIKeysService {

    /**
     * Fetches an API key for a given identifier.
     *
     * @param {string} id - The identifier for which the API key needs to be fetched.
     * @return {Promise<AxiosResponse<APIKey>>} A promise resolving to the AxiosResponse containing the APIKey.
     */
    public static async fetchAPIKey(id: string): Promise<AxiosResponse<APIKey>> {
        return apiClient.get(PARKING_ENDPOINTS.FETCH_API_KEY(id));
    }

    /**
     * Searches for API keys based on the provided filter criteria.
     *
     * @param {Object} filter - The filter criteria for searching API keys.
     * @param {number} filter.page - The current page number.
     * @param {number} filter.size - The number of items per page.
     * @param {string} filter.id - The unique identifier of the API key.
     * @param {string} filter.status - The status of the API key.
     * @param {string} filter.parkingId - The parking ID associated with the API key.
     * @return {Promise<AxiosResponse<Page<APIKeyListElement>>>} A promise that resolves to the response containing the paginated list of API keys.
     */
    public static async searchAPIKey(filter: {
        page: number;
        size: number;
        id: string;
        status: string;
        parkingId: string;
    }): Promise<AxiosResponse<Page<APIKeyListElement>>> {
        return apiClient.get(PARKING_ENDPOINTS.SEARCH_API_KEY(), {params: filter})
    }

    /**
     * Generates a new API key for a specific parking ID.
     *
     * @param {string} parkingId - The unique identifier of the parking lot for which the API key is being generated.
     * @return {Promise<AxiosResponse<APIKey>>} - A promise that resolves to the Axios response containing the generated API key.
     */
    public static async generateAPIKey(parkingId: string): Promise<AxiosResponse<APIKey>> {
        return apiClient.post(PARKING_ENDPOINTS.GENERATE(parkingId), {scopes: ["PARKING"]});
    }

    /**
     * Revokes the API key associated with the specified parking ID.
     *
     * @param {string} parkingId - The unique identifier of the parking lot whose API key should be revoked.
     * @return {Promise<AxiosResponse<APIKey>>} A promise that resolves to the response containing the revoked API key.
     */
    public static async revokeAPIKey(parkingId: string): Promise<AxiosResponse<APIKey>> {
        return apiClient.post(PARKING_ENDPOINTS.REVOKE(parkingId));
    }
}