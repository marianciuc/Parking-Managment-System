import {Page, Parking, ParkingListItem} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const PARKING_ENDPOINTS = {
    SEARCH_PARKING: () => `/api/v1/parking/search`,
    FETCH_PARKING: (id: string) => `/api/v1/parking/${id}`,
};


export default class ParkingService {

    public static async fetchDriverDetails(parkingId: string): Promise<AxiosResponse<Parking>> {
        return apiClient.get(PARKING_ENDPOINTS.FETCH_PARKING(parkingId));
    }

    public static async searchParking(filter: {
        page: number;
        size: number;
        id: string;
        country: string;
        city: string;
        ownerId: string;
        recordStatus: string;
        status: string;
        name: string;
    }): Promise<AxiosResponse<Page<ParkingListItem>>> {
        return apiClient.get(PARKING_ENDPOINTS.SEARCH_PARKING(), {params: filter})
    }
}