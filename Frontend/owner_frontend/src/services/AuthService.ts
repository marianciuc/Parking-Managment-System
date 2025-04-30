import $api, { setTokenType } from "@/http";
import {AxiosResponse} from "axios";
import {AuthResponse} from "@/models/response/AuthRespons.ts";
import {CredentialsRequest} from "@/models/requests";

const USER_TYPE = 'PARKING_OWNER';
const BASE_PATH = '/api/v1/security';

const use_mock = import.meta.env.VITE_USE_MOCK as boolean;


export default class AuthService {
    static async login(req: CredentialsRequest): Promise<AxiosResponse<AuthResponse>> {
        if(use_mock){
            return Promise.resolve({
                data: {
                    accessToken: "mocked_access_token",
                    accessTokenExpiry: "2025-02-25T11:59:42.769976Z",
                    refreshToken: "mocked_refresh_token",
                    refreshTokenExpiry: "2025-02-26T11:54:42.768853Z",
                },
                status: 201,
                statusText: "Created",
                headers: {},
                config: {},
            } as AxiosResponse<AuthResponse>);
        }

        return $api.post<AuthResponse>(`${BASE_PATH}/login`, req)
    }

    static async registration(req: CredentialsRequest): Promise<AxiosResponse<AuthResponse>> {

        if(use_mock){
            return Promise.resolve({
                data: {
                    accessToken: "eyJraWQiOiJhMThiNWIzMy03ZWVhLTQ3ZDItYjAxZS04NWE4ZjkyNzgzMTgiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJoYW5keS1wYXJraW5nLXNlY3VyaXR5LXNlcnZpY2UiLCJzdWIiOiIwMjNjMmYyYi0zNzdkLTRmOGMtOWM4My0xYzFmYzlhMjhhYTciLCJleHAiOjE3NDE0ODI5NDQsImlhdCI6MTc0MTQ4MjY0NCwianRpIjoiYTE4YjViMzMtN2VlYS00N2QyLWIwMWUtODVhOGY5Mjc4MzE4IiwiYXV0aG9yaXRpZXMiOlsiVVBEQVRFX1BBUktJTkciLCJDUkVBVEVfUEFSS0lORyIsIlBBUktJTkdfREVMRVRFX1JFUVVFU1QiLCJMT0dJTl9JTl9QQVJLSU5HX1BBTkVMIiwiUkVGUkVTSF9UT0tFTiIsIlJFRlJFU0hfVE9LRU4iXX0.LsokIqmHjZ3P5-UNJefZ35-uImPy2Ob2kNRdPpTWnbDg26Mdz2dEpXj1GCqwc_9uGYWc-7BWhtTe_yeGD7fXoK88e053fNwjUCwrDrYKSGZ41s6fFcAGXPZGgpEWfuxXacouom5mwBUAfbtVkdMuIcpyOVQFPf2XEx75oDzofzN6PfTgaF4c5R0uBzRmHYpmG3sOzci2DQtR5N-yg8os-yE8nGzuNDI8Ox9mbjr0frh_cxUG2GAldLNwvbbfLzbZseenwi-Pg62LgeZTfeKbxXCPMVkMRskYQEktTEU-IMRr9w5Q4u3I1fwy2qtg_bygqia2-Pirs2c0VW8AhvZMSA",
                    accessTokenExpiry: "2025-02-25T11:59:42.769976Z",
                    refreshToken: "eyJraWQiOiI1N2YyZWRiNi1kMWUzLTQ3YjAtYTIzYi1iMTY5ZGU2NTcxNjkiLCJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.n6AwnF4DhPpM9W8I6GKDIxe5y2pUfbPn2bFQcfrjIOdAybTVlLeR8zRkdOd9IZ_3_TR7h5ogV6IdDU6jPBMnX6jDJnvoOKyq-ckUcTebCC6ER5veahBnkEEc1RWHMxgfaQ4Jah_fx7aX7NdawDLWYopvPW8SMCVh-BPJCKXqRJfWpLjY0VTWVLvASuse2PrOFlQKutiwGBYHVpvWejx5uG_raouBw3cGlrCOXh2lCfRJtONP_RggZLRaFGu0EoBFLtXUzhhbGdIg3RwL0f_7V9purO-JjDHUhW8ZiMqbiJob314Y8J4a-w4475M8tbky20bhew16alZoq3XydwzS7g.tBNAN1s1oQAPUKBl.P-8BSJpjUls2WPL-REXjgnHfyWjRkdj2_gYBvDWvpYhu48rw5JeK1csmboGg680vIMHopyKZyFOQo4NpqaNY5O_PriHdR0G99D1xkcrwU-IYA5z0hb988JWYdUeIZ_sfgwZSg7QDk6PvOPEs6SQcxFQm-OCpKLhQdY8fXYMrpGUBXdyC0WDJ36GwqJ_bCqbF4iiePWOI0vg1BlHKgrD6CwBc4yk0uC4G9pc0SS5guCgz6mPnIJM1_4xLxPFBaXuj-LOBCSO-SCkFR4zxxqNjcCUMqbz6uqsE6B43aGXvk5U6ELTHVGtrBs4EBi94YN8Od7hx7J_9ZdOgsZJiCeq2KP1wPmHFSMLGxcvUiNesskn8UFFgHwRBYSTIfK9wvkUUC6eWxK5wG7q75G-0-tjLGBfuKY5Y7JyxWcFv4finoecTKxbTqfan14Jmbg.D4JvJc8RIDYOIr1ZKTIlrg",
                    refreshTokenExpiry: "2025-02-26T11:54:42.768853Z",
                },
                status: 201,
                statusText: "Created",
                headers: {},
                config: {},
            } as AxiosResponse<AuthResponse>);
        }


        return $api.post<AuthResponse>(`${BASE_PATH}/register/${USER_TYPE}`, req)
    }

    static async logout(): Promise<void> {
        setTokenType("refresh")
        return $api.post(`${BASE_PATH}/block-token`)
    }

    static async refreshToken() {
        setTokenType("refresh")
        if(use_mock){
            return Promise.resolve({
                data: {
                    accessToken: "eyJraWQiOiJhMThiNWIzMy03ZWVhLTQ3ZDItYjAxZS04NWE4ZjkyNzgzMTgiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJoYW5keS1wYXJraW5nLXNlY3VyaXR5LXNlcnZpY2UiLCJzdWIiOiIwMjNjMmYyYi0zNzdkLTRmOGMtOWM4My0xYzFmYzlhMjhhYTciLCJleHAiOjE3NDE0ODI5NDQsImlhdCI6MTc0MTQ4MjY0NCwianRpIjoiYTE4YjViMzMtN2VlYS00N2QyLWIwMWUtODVhOGY5Mjc4MzE4IiwiYXV0aG9yaXRpZXMiOlsiVVBEQVRFX1BBUktJTkciLCJDUkVBVEVfUEFSS0lORyIsIlBBUktJTkdfREVMRVRFX1JFUVVFU1QiLCJMT0dJTl9JTl9QQVJLSU5HX1BBTkVMIiwiUkVGUkVTSF9UT0tFTiIsIlJFRlJFU0hfVE9LRU4iXX0.LsokIqmHjZ3P5-UNJefZ35-uImPy2Ob2kNRdPpTWnbDg26Mdz2dEpXj1GCqwc_9uGYWc-7BWhtTe_yeGD7fXoK88e053fNwjUCwrDrYKSGZ41s6fFcAGXPZGgpEWfuxXacouom5mwBUAfbtVkdMuIcpyOVQFPf2XEx75oDzofzN6PfTgaF4c5R0uBzRmHYpmG3sOzci2DQtR5N-yg8os-yE8nGzuNDI8Ox9mbjr0frh_cxUG2GAldLNwvbbfLzbZseenwi-Pg62LgeZTfeKbxXCPMVkMRskYQEktTEU-IMRr9w5Q4u3I1fwy2qtg_bygqia2-Pirs2c0VW8AhvZMSA",
                    accessTokenExpiry: "2025-02-25T11:59:42.769976Z",
                    refreshToken: "eyJraWQiOiI1N2YyZWRiNi1kMWUzLTQ3YjAtYTIzYi1iMTY5ZGU2NTcxNjkiLCJjdHkiOiJKV1QiLCJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAtMjU2In0.n6AwnF4DhPpM9W8I6GKDIxe5y2pUfbPn2bFQcfrjIOdAybTVlLeR8zRkdOd9IZ_3_TR7h5ogV6IdDU6jPBMnX6jDJnvoOKyq-ckUcTebCC6ER5veahBnkEEc1RWHMxgfaQ4Jah_fx7aX7NdawDLWYopvPW8SMCVh-BPJCKXqRJfWpLjY0VTWVLvASuse2PrOFlQKutiwGBYHVpvWejx5uG_raouBw3cGlrCOXh2lCfRJtONP_RggZLRaFGu0EoBFLtXUzhhbGdIg3RwL0f_7V9purO-JjDHUhW8ZiMqbiJob314Y8J4a-w4475M8tbky20bhew16alZoq3XydwzS7g.tBNAN1s1oQAPUKBl.P-8BSJpjUls2WPL-REXjgnHfyWjRkdj2_gYBvDWvpYhu48rw5JeK1csmboGg680vIMHopyKZyFOQo4NpqaNY5O_PriHdR0G99D1xkcrwU-IYA5z0hb988JWYdUeIZ_sfgwZSg7QDk6PvOPEs6SQcxFQm-OCpKLhQdY8fXYMrpGUBXdyC0WDJ36GwqJ_bCqbF4iiePWOI0vg1BlHKgrD6CwBc4yk0uC4G9pc0SS5guCgz6mPnIJM1_4xLxPFBaXuj-LOBCSO-SCkFR4zxxqNjcCUMqbz6uqsE6B43aGXvk5U6ELTHVGtrBs4EBi94YN8Od7hx7J_9ZdOgsZJiCeq2KP1wPmHFSMLGxcvUiNesskn8UFFgHwRBYSTIfK9wvkUUC6eWxK5wG7q75G-0-tjLGBfuKY5Y7JyxWcFv4finoecTKxbTqfan14Jmbg.D4JvJc8RIDYOIr1ZKTIlrg",
                    refreshTokenExpiry: "2025-02-26T11:54:42.768853Z",
                },
                status: 201,
                statusText: "Created",
                headers: {},
                config: {},
            } as AxiosResponse<AuthResponse>);
        }

        return $api.post(`${BASE_PATH}/jwt/refresh`)
    }

    static async changePassword(oldPassword: string, newPassword: string): Promise<void> {
        return $api.put(`/api/v1/users`, {oldPassword, newPassword})
    }
}
