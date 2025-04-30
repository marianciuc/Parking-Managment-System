import axios from "axios";
import {getCookie} from "@/lib/cookies";

const TOKEN_TYPE_ACCESS = "access";
const TOKEN_TYPE_REFRESH = "refresh";
const TOKEN_KEY_ACCESS = "accessToken";
const TOKEN_KEY_REFRESH = "refreshToken";

const apiClient = axios.create({
    withCredentials: true,
    baseURL: "http://localhost:8222",
});

/**
 * Sets the "Token-Type" header for the API client with the specified token type.
 *
 * @param {"access" | "refresh"} type - The type of token to set in the "Token-Type" header.
 */
export const setTokenHeader = (type: "access" | "refresh") => {
    apiClient.defaults.headers.common["Token-Type"] = type;
};

const attachAuthorizationHeader = (config: import("axios").AxiosRequestConfig) => {
    const tokenType = config.headers?.["Token-Type"] || TOKEN_TYPE_ACCESS;
    const tokenKey = tokenType === TOKEN_TYPE_REFRESH ? TOKEN_KEY_REFRESH : TOKEN_KEY_ACCESS;
    const token = getCookie(tokenKey);

    if (token) {
        config.headers = config.headers || {};
        config.headers.Authorization = `Bearer ${token}`;
    }
    if (config.headers) {
        delete config.headers["Token-Type"];
    }
    return config;
};

apiClient.interceptors.request.use((config) => {
    config.headers = config.headers || {};
    return attachAuthorizationHeader(config) as import("axios").InternalAxiosRequestConfig;
});

export default apiClient;