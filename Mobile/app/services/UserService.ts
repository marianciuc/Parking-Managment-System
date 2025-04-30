import api, { setTokenType } from "@/app/http/api";
import IAuthResponse from "@/app/interfaces/IAuthResponse";
import {getAccessToken, getRefreshToken, setAccessToken, setRefreshToken} from "@/app/utils/SecureStore";

const getUser = async() => {
    setTokenType("access");
    return api.get(`/api/v1/drivers`);
}

export {getUser}


