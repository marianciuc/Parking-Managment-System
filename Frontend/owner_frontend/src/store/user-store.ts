import {makeAutoObservable} from "mobx";
import {AxiosError} from "axios";
import AuthService from "@/services/AuthService.ts";
import {CreateParkingRequest, CredentialsRequest} from "@/models/requests";
import ParkingService from "@/services/ParkingService.ts";
import {IError, IUser} from "@/models/common";
import {AuthResponse} from "@/models/response";
import UserService from "@/services/UserService.ts";
import {
    ReqRegistrationStep1,
    ReqRegistrationStep2,
    ReqUpdateAddress,
    ReqUpdatePersonalInformation
} from "@/models/requests/ReqRegistrationStep1.ts";

export default class UserStore {
    user = {} as IUser;
    isAuth: boolean = false;
    isLoading: boolean = false;
    userCurrency: string = "USD";

    constructor() {
        makeAutoObservable(this);
    }

    setAuth(bool: boolean) {
        this.isAuth = bool;
    }

    setUser(user: IUser) {
        this.user = user;
    }

    setIsLoading(isLoading: boolean) {
        this.isLoading = isLoading;
    }

    async fetchUserData() {
        try {
            const req = await UserService.fetchUser();
            this.setUser(req.data);
        } catch (e: unknown) {
            const error = e as AxiosError;
            throw {
                message: ((error.response?.data as { message: string })?.message || "Unknown error"),
                title: "Authentication error",
            } as IError;
        }
    }

    async login(req: CredentialsRequest) {
        try {
            const res = await AuthService.login(req);
            this.insertTokensToLocalStorage(res.data);
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
            throw {
                message: ((error.response?.data as { message: string })?.message || "Unknown error"),
                title: "Authentication error",
            } as IError;
        }
    }


    async registration(req: CredentialsRequest) {
        try {
            const res = await AuthService.registration(req);
            this.insertTokensToLocalStorage(res.data);
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
            throw {
                message: ((error.response?.data as { message: string })?.message || "Unknown error"),
                title: "Authentication error",
            } as IError;
        }
    }

    async changPassword(oldPassword:string, newPassword:string) {
        try {
            await AuthService.changePassword(oldPassword, newPassword);

        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
            throw {
                message: ((error.response?.data as { message: string })?.message || "Unknown error"),
                title: "Authentication error",
            } as IError;
        }
    }

    async logout() {
        try {
            await AuthService.logout();
            this.removeTokensFromLocalStorage();
            this.setUser({} as IUser);
            
            //remove all item from localStorage
            // localStorage.clear();
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }

    async processRegistrationStep1(req: ReqRegistrationStep1) {
        try {
            await UserService.registerStep1(req);
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }

    async processRegistrationStep2(req: ReqRegistrationStep2) {
        try {
            await UserService.registerStep2(req);
            await this.fetchUserData();
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }

    async updateAddress(req: ReqUpdateAddress) {
        try{
            await UserService.updateAddress(this.user.userId,req);
            await this.fetchUserData();
        }catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }
    async updatePersonalInformation(req: ReqUpdatePersonalInformation) {
        try{
            await UserService.updatePersonalInformation(this.user.userId,req);
            await this.fetchUserData();
        }catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }

    async createParking(req: CreateParkingRequest) {
        try {
            console.log(req);
            const res = await ParkingService.createParking(req)
            console.log(res);
            return res;
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log(error.response?.data);
        }
    }


    async checkAuth() {
        this.setIsLoading(true);
        // try{
        //     const res = await axios.get<AuthResponse>(`${SERVER_URL}/refresh`, {withCredentials:true});
        //     console.log(res);
        //     localStorage.setItem('token', res.data.accessToken);
        this.setAuth(true);
        //
        // }catch (e: unknown) {
        //     const error = e as AxiosError;
        //     console.log(error.response?.data);
        // } finally {
        this.setIsLoading(false);
        // }
    }

    private insertTokensToLocalStorage(response: AuthResponse): void {
        localStorage.setItem("accessToken", response.accessToken);
        localStorage.setItem("refreshToken", response.refreshToken);
        this.setAuth(true);
    }

    private removeTokensFromLocalStorage(): void {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        localStorage.removeItem("active-parking")
        this.setAuth(false);
    }

    async validateAuthentication(): Promise<boolean> {

        console.log("Start validation" + this.isAuth);
        // if (this.isAuth && localStorage.getItem("accessToken")) {
        //     this.setIsLoading(true);
        //     const fetchedUser = await UserService.fetchUser();
        //     this.setUser(fetchedUser.data);
        //     this.setIsLoading(false);
        //     return true;
        // }
        console.log("validateAuthentication");

        this.setIsLoading(true);

        const refreshToken = localStorage.getItem("refreshToken");
        console.log("refreshToken", refreshToken);

        if (localStorage.getItem("refreshToken")) {
            try {
                const res = await AuthService.refreshToken();

                const permissionsResponse = await UserService.checkPermission();
                if (permissionsResponse.status !== 200) {
                    console.log("Insufficient permissions, logging out...");
                    await this.logout();
                    return false;
                }

                console.log("Refresh token request success:", res.data);

                const fetchedUser = await UserService.fetchUser();

                this.insertTokensToLocalStorage(res.data);
                this.setUser(fetchedUser.data);
                this.setAuth(true);
                console.log("User data:", fetchedUser);
                return true;
            } catch (e: unknown) {
                const error = e as AxiosError;
                console.log("Refresh token request failed:", error.response?.data);
                return false;
            } finally {
                this.setIsLoading(false);
            }
        }

        this.setIsLoading(false);
        return false;
    }


}