import {makeAutoObservable} from "mobx";
import {AxiosError} from "axios";
import { IAuthResponse, IUser} from "@/app/interfaces";
import * as AuthService from "@/app/services/AuthService";
import * as UserService from "@/app/services/UserService";
import { getRefreshToken, setAccessToken, setRefreshToken} from "@/app/utils/SecureStore";
import handleError from "@/app/utils/ErrorHandler";
import {fetchVerification} from "../services/VerificationService";

export default class UserStore {
    isAuthenticated: boolean = false;
    isLoading: boolean = true;
    user: IUser | null = null;
    isVerified: boolean = false;

    constructor() {
        makeAutoObservable(this);
    }

    setIsAuthenticated(isAuthenticated: boolean, authentication: IAuthResponse) {
        setAccessToken(authentication.accessToken).then(
            () => console.log("setAccessToken success"));
        setRefreshToken(authentication.refreshToken).then(
            () => console.log("setRefreshToken success"));
        console.log(
            "setIsAuthenticated",
            isAuthenticated,
            authentication
        );
        this.isAuthenticated = isAuthenticated;
    }

    setIsLoading(isLoading: boolean) {
        this.isLoading = isLoading;
    }

    setIsVerified(isVerified: boolean) {
        this.isVerified = isVerified;
    }

    signUpAuthentication = async ({email, password}: { email: string, password: string }) => {
        this.setIsLoading(true);

        console.log("signUpAuthentication");
        try {
            const request = await AuthService.signUpByCredentials({email, password});
            if (request.data) {
                const authResponse: IAuthResponse = request.data;
                this.setIsAuthenticated(true, authResponse)
                console.log("authResponse", authResponse);
            } else {
                throw new AxiosError("unexpected message");
            }
        } catch (e: unknown) {
            handleError(e, "awibka");
        } finally {
            this.isLoading = false;
        }
    }

    signInAuthentication = async ({email, password}: { email: string, password: string }) => {
        this.setIsLoading(true);

        try {
            const request = await AuthService.signInByCredentials({email, password});
            if (request.data) {
                const authResponse: IAuthResponse = request.data;
                this.setIsAuthenticated(true, authResponse)
            } else {
                throw new AxiosError("unexpected message");
            }
        } catch (e: unknown) {
            handleError(e, "awibka");
        } finally {
            this.isLoading = false;
        }
    }

    signInAuthenticationOAuth = async (url: string) => {
        this.setIsLoading(true);

        try {
            const request = await AuthService.authWithExternalAuthProvider(url);
            if (request.data) {
                const authResponse: IAuthResponse = request.data;
                this.setIsAuthenticated(true, authResponse)
            } else {
                throw new AxiosError("unexpected message");
            }
        } catch (e: unknown) {
            handleError(e, "Error on Google Authentication ");
        } finally {
            this.isLoading = false;
        }
    }

    async verifyUser(code: string) {
        try {
            await fetchVerification(code);

            if (this.user) {
                this.user.isEmailVerified = true; // Обновляем объект пользователя
            }
            this.setIsVerified(true); // Обновляем состояние в сторе

            console.log("User verified successfully");
        } catch (e: unknown) {
            handleError(e, "Error verifying user");
        }
    }

    setUser = (user: IUser) => {
        this.user = user;
        this.setIsVerified(user.isEmailVerified);
    }

    getUserId(): string {
        return this.user?.userId || '';
    }

    logout = async () => {
        try {
            this.isAuthenticated = false;
            setAccessToken("");
            setRefreshToken("");
            console.log("User logged out successfully");
        } catch (e: unknown) {
            handleError(e, "Logout error");
        }
    }

    fetchUser = async () => {
        try {

            const fetchedUser = await UserService.getUser();

            console.log("User data:", fetchedUser);
            this.setUser(fetchedUser.data);

            return true;
        } catch (e: unknown) {
            handleError(e, "awibka");
            const error = e as AxiosError;
            console.log("Refresh token request failed:", error.response?.data);
            return false;
        }
    }


    async validateAuthentication(): Promise<boolean> {

        console.log("Start validation" + this.isAuthenticated);

        console.log("validateAuthentication");

        this.setIsLoading(true);

        const refreshToken = await getRefreshToken();
        console.log("refreshToken", refreshToken);

        if (refreshToken) {
            try {
                const res = await AuthService.refreshToken({refreshToken});
                this.setIsAuthenticated(true, res.data);

                const fetchedUser = await UserService.getUser();

                console.log("User data:", fetchedUser);
                this.setUser(fetchedUser.data);

                return true;
            } catch (e: unknown) {
                handleError(e, "awibka");
                const error = e as AxiosError;
                console.log("Refresh token request failed:", error.response?.data);
                this.logout();
                return false;
            } finally {
                this.setIsLoading(false);
            }
        }

        this.setIsLoading(false);
        return false;
    }
}