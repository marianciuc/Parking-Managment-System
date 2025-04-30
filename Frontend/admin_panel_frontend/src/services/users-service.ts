import {Page, UserDomain} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient from "@/http";

const USERS_DETAILS_ENDPOINTS = {
    SEARCH_USERS: () => `/api/v1/users/search`,
    BLOCK_USER: (id: string) => `/api/v1/users/${id}/block`,
    UNBLOCK_USER: (id: string) => `/api/v1/users/${id}/unblock`,
};


export default class UsersService {

    public static async searchUsers(filter: {
        email: string;
        page: number;
        size: number;
        recordStatus: string;
        userType: string;
    }): Promise<AxiosResponse<Page<UserDomain>>> {
        return apiClient.get(USERS_DETAILS_ENDPOINTS.SEARCH_USERS(), {params: filter})
    }

    public static async banUser(id: string): Promise<AxiosResponse<UserDomain>> {
        return apiClient.get(USERS_DETAILS_ENDPOINTS.BLOCK_USER(id))
    }

    public static async unbanUser(id: string): Promise<AxiosResponse<UserDomain>> {
        return apiClient.get(USERS_DETAILS_ENDPOINTS.UNBLOCK_USER(id))
    }
}