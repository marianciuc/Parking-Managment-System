import {OwnerDomain, Page} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const OWNER_DETAILS_ENDPOINTS = {
    SEARCH_OWNERS: () => `/api/v1/drivers/search`,
    FETCH_OWNER: (id: string) => `/api/v1/drivers/${id}`,
};


export default class DriverDetailsService {

    public static async fetchDriverDetails(ownerId: string): Promise<AxiosResponse<OwnerDomain>> {
        return apiClient.get(OWNER_DETAILS_ENDPOINTS.FETCH_OWNER(ownerId));
    }

    public static async searchDrivers(filter: {
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