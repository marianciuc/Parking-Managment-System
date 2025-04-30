import axios from "axios";

// export const SERVER_URL = "http://localhost:8222";
// export const SERVER_URL = "https://moth-next-partly.ngrok-free.app";
export const SERVER_URL = "http://localhost:8222"

const $api = axios.create({
    withCredentials: true,
    baseURL: SERVER_URL,
});

export const setTokenType = (type: "access" | "refresh") => {
    $api.defaults.headers.common["Token-Type"] = type;
};

$api.interceptors.request.use((config) => {
    config.headers = config.headers || {};

    const tokenType = config.headers["Token-Type"] || "access"; // Default to access token
    const tokenKey = tokenType === "refresh" ? "refreshToken" : "accessToken";
    const token = localStorage.getItem(tokenKey);

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    // Clean up Token-Type header to avoid sending unnecessary data
    delete config.headers["Token-Type"];

    return config;
});

export default $api;