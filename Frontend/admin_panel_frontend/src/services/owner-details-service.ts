import {OwnerDomain, Page} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const OWNER_DETAILS_ENDPOINTS = {
    SEARCH_OWNERS: () => `/api/v1/owners/details/search`,
    FETCH_OWNER: (id: string) => `/api/v1/owners/${id}/details`,
};


export default class OwnerDetailsService {

    public static async fetchOwnerDetails(ownerId: string): Promise<AxiosResponse<OwnerDomain>> {
        return apiClient.get(OWNER_DETAILS_ENDPOINTS.FETCH_OWNER(ownerId));
    }

    public static async searchOwners(filter: {
        email: string;
        page: number;
        size: number;
        id: string;
        country: string;
        city: string;
        phoneNumber: string
    }): Promise<AxiosResponse<Page<OwnerDomain>>> {
        return apiClient.get(OWNER_DETAILS_ENDPOINTS.SEARCH_OWNERS(), {params: filter})
    }
}