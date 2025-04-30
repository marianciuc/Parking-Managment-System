import api, {setTokenType} from "@/app/http/api";

const fetchVerification = async (code:string) => {
    return api.patch(`/api/v1/drivers/email-verification?code=${code}`);
}

const sendVerificationCode = async () => {
    return api.post(`/api/v1/drivers/email-verification`);
}

export {fetchVerification, sendVerificationCode};