import api, { setTokenType } from "@/app/http/api";
import {AxiosResponse} from "axios";

const use_mock = process.env.EXPO_PUBLIC_USE_MOCK;

const signInByCredentials = async ({email, password}: { email: string, password: string }) => {
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
        } as AxiosResponse<any>);
    }
    return api.post(`/api/v1/security/login`, {email, password});
}

const signUpByCredentials = async ({email, password}: { email: string, password: string }) => {
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
        } as AxiosResponse<any>);
    }

    return api.post(`/api/v1/security/register/DRIVER`, {email, password});
}

const authWithExternalAuthProvider = async (url:string) => {
    return api.post(url);
}

const refreshToken = async ({refreshToken}:{refreshToken:string}) => {
    setTokenType("refresh");
    return api.post(`/api/v1/security/jwt/refresh`, {refreshToken});
}

async function getGoogleSignInURL():Promise<AxiosResponse<any>> {
    return api.get(`/api/oauth2/google/url`);
}

export {signInByCredentials, signUpByCredentials,refreshToken,getGoogleSignInURL,authWithExternalAuthProvider};


