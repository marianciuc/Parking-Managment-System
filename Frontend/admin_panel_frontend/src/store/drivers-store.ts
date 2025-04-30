import {AdministratorDomain, AuthResponse, Credentials, ToastMessage} from "@/interfaces";
import {ToastMessageType} from "@/interfaces/toast-message";
import {makeAutoObservable} from "mobx";
import AuthService from "@/services/auth-service";
import AdministratorsDetailsService from "@/services/administrators-details-service";
import {handleAxiosError} from "@/lib/exception-handler";
import { setCookie, deleteCookie } from "@/lib/cookies";


/**
 * The `UserStore` class manages the state and behavior of an authenticated user.
 * It handles user authentication, permissions, and logout functionality,
 * and interacts with authentication and user details services.
 */
export default class UserStore {
    private user: AdministratorDomain | null = null;
    private isAuthenticated: boolean = false;


    public get authenticatedUser(): AdministratorDomain | null {
        return this.user;
    }

    public get isAuthenticatedUser(): boolean {
        return this.isAuthenticated;
    }

    public constructor() {
        makeAutoObservable(this);
    }

    private setAuthenticatedUser(administratorDomain: AdministratorDomain) {
        this.user = administratorDomain;
    }

    private setAuthentication(authResponse: AuthResponse) {
        setCookie("accessToken", authResponse.accessToken, { httpOnly: false });
        setCookie("refreshToken", authResponse.refreshToken, { httpOnly: false });

        this.isAuthenticated = true;
    }

    public logout(): void {
        deleteCookie("accessToken");
        deleteCookie("refreshToken");
        this.isAuthenticated = false;
        this.user = null;
    }

    /**
     * Handles user login by authenticating the provided credentials,
     * verifying permissions, and returning an appropriate toast message.
     *
     * @param {Credentials} credential - The credentials object containing authentication information such as username and password.
     * @return {Promise<ToastMessage>} A promise that resolves to a ToastMessage object indicating the success or failure of the login attempt.
     */
    public async login(credential: Credentials): Promise<ToastMessage> {
        try {
            const res = await AuthService.signIn(credential);
            const authResponse: AuthResponse = res.data;
            this.setAuthentication(authResponse);

            const administratorHasPermission: boolean = await this.verifyPermissions();
            if (!administratorHasPermission) {
                this.logout();
                return {
                    messageType: "error" as ToastMessageType,
                    message: "You don't have permission to access this resource",
                    statusCode: 403,
                } as ToastMessage;

            }
            return {
                messageType: "success" as ToastMessageType,
                message: "Login successful",
                statusCode: 200,
            } as ToastMessage;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    /**
     * Verifies if the current user has the necessary permissions by invoking the AdministratorsDetailsService.checkPermissions method.
     *
     * @return {Promise<boolean>} A promise that resolves to true if the permissions are valid, or false if the verification fails.
     */
    private async verifyPermissions(): Promise<boolean> {
        try {
            await AdministratorsDetailsService.checkPermissions();
            return true;
        } catch (error: unknown) {
            console.error(error);
            return false;
        }
    }

    /**
     * Handles the process of refreshing the user's access token and verifying their permissions.
     * This method checks for authentication status and performs token refresh if necessary.
     * It also validates user permissions and, if authentication or permissions fail, it logs the user out.
     *
     * @return {Promise<ToastMessage | void>} A promise that resolves to a ToastMessage object containing the status of the operation
     * if applicable, or void if no action is taken or on failure.
     */
    public async refreshAccess(): Promise<ToastMessage | void> {
        if (!this.isAuthenticated && localStorage.getItem("refreshToken") === null && localStorage.getItem("accessToken") === null) {
            return;
        }

        try {
            const res = await AuthService.refreshToken();
            const authResponse: AuthResponse = res.data;

            this.setAuthentication(authResponse);
            const administratorHasPermission: boolean = await this.verifyPermissions();

            if (!administratorHasPermission) {
                this.logout();
                return {
                    messageType: "error" as ToastMessageType,
                    message: "You don't have permission to access this resource",
                    statusCode: 403,
                } as ToastMessage;

            }
            return {
                messageType: "success" as ToastMessageType,
                message: "Login successful",
                statusCode: 200,
            } as ToastMessage;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    /**
     * Registers a user using the provided credentials and handles authentication.
     *
     * @param {Credentials} credential - The user's registration credentials, including required fields such as username and password.
     * @return {Promise<ToastMessage>} A promise resolving to an object containing a message type, message, and status code.
     * It indicates whether the operation succeeded or failed, along with relevant information.
     */
    public async signUp(credential: Credentials): Promise<ToastMessage> {
        try {
            const res = await AuthService.register(credential);
            const authResponse: AuthResponse = res.data;

            this.setAuthentication(authResponse);
            const administratorHasPermission: boolean = await this.verifyPermissions();

            if (!administratorHasPermission) {
                this.logout();
                return {
                    messageType: "error" as ToastMessageType,
                    message: "You don't have permission to access this resource",
                    statusCode: 403,
                } as ToastMessage;

            }
            return {
                messageType: "success" as ToastMessageType,
                message: "Login successful",
                statusCode: 200,
            } as ToastMessage;
        } catch (err) {
            return handleAxiosError(err);
        }
    }

    /**
     * Fetches the details of the currently authenticated user.
     * The information is retrieved from the AdministratorDetailsService and updates
     * the state of the application with authenticated user details if successful.
     * In case of an error, it handles the Axios error and returns an appropriate
     * ToastMessage with the error description.
     *
     * @return {Promise<ToastMessage | undefined>} A Promise that resolves to a ToastMessage
     * in case of an error or undefined in case of successful execution.
     */
    public async fetchAuthenticatedUser(): Promise<ToastMessage | undefined> {
        try {
            const res = await AdministratorsDetailsService.fetchAuthenticatedAdministratorDetails();
            const administratorDomain: AdministratorDomain = res.data;
            this.setAuthenticatedUser(administratorDomain);
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }
}

