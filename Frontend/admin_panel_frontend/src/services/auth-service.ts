import {AuthResponse, Credentials} from "@/interfaces"
import {AxiosResponse} from "axios";
import apiClient, {setTokenHeader} from "@/http";
import {TokenType} from "@/http/token-type";

const AUTH_ENDPOINTS = {
    LOGIN: `/api/v1/security/login`,
    LOGOUT: `/api/v1/security/logout`,
    REFRESH: `/api/v1/security/jwt/refresh`,
    REGISTER: `/api/v1/security/register/ADMIN`,
};


/**
 * The AuthService class provides methods for handling authentication-related operations such as
 * signing in, signing out, refreshing tokens, and user registration.
 */
export default class AuthService {

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param {Credentials} credentials - The user's login credentials, including email and password.
     * @return {Promise<AxiosResponse<AuthResponse>>} A promise that resolves to the authentication response.
     */
    public static async signIn(credentials: Credentials): Promise<AxiosResponse<AuthResponse>> {
        return apiClient.post(AUTH_ENDPOINTS.LOGIN, credentials);
    }

    /**
     * Signs out the current user by sending a POST request to the logout endpoint.
     * @return {Promise<AxiosResponse<void>>} A promise resolving with the server response indicating the success of the sign-out operation.
     */
    public static async signOut(): Promise<AxiosResponse<void>> {
        return apiClient.post(AUTH_ENDPOINTS.LOGOUT);
    }

    /**
     * Refreshes the authentication token by sending a request to the refresh token endpoint.
     *
     * @return {Promise<AxiosResponse<AuthResponse>>} A promise resolving to the server response containing the new authentication token.
     */
    public static async refreshToken(): Promise<AxiosResponse<AuthResponse>> {
        setTokenHeader(TokenType.REFRESH);
        return apiClient.post(AUTH_ENDPOINTS.REFRESH);
    }

    /**
     * Registers a new user with the provided credentials.
     *
     * @param {Credentials} credentials - The user registration details, including required information such as username, password, and other relevant fields.
     * @return {Promise<AxiosResponse<AuthResponse>>} A promise that resolves to the server response containing authentication details or an error.
     */
    public static async register(credentials: Credentials): Promise<AxiosResponse<AuthResponse>> {
        return apiClient.post(AUTH_ENDPOINTS.REGISTER, credentials);
    }
}