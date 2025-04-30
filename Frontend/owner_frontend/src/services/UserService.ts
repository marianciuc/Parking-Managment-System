import $api, {setTokenType} from "@/http";
import {AxiosResponse} from "axios";
import {IUser} from "@/models/common/IUser.ts";
import {
    ReqRegistrationStep1,
    ReqRegistrationStep2,
    ReqUpdateAddress,
    ReqUpdatePersonalInformation
} from "@/models/requests/ReqRegistrationStep1.ts";


const use_mock = import.meta.env.VITE_USE_MOCK;

export default class UserService {

    static BASE_PATH = '/api/v1/owners';

    static async fetchUser(): Promise<AxiosResponse<IUser>> {
        setTokenType("access")
        if(use_mock){
           return Promise.resolve({data:{
                   "id": "b136d10c-58f3-4c9e-9059-bdd4f17fecf1",
                   "firstName": "Vitalii",
                   "middleName": "",
                   "lastName": "Natalevych",
                   "userId": "023c2f2b-377d-4f8c-9c83-1c1fc9a28aa7",
                   "NIP": null,
                   "phoneNumber": "098673945",
                   "phoneNumberCode": "+48",
                   "address": {
                       "country": "Poland",
                       "city": "Szczecin",
                       "street": "Chopina",
                       "houseNumber": "55e",
                       "apartmentNumber": "55",
                       "postalCode": "71450"
                   },
                   "isRegistrationCompleted": true,
                   "dateOfBirth": [
                       2025,
                       3,
                       2
                   ],
                   "creationDate": [
                       2025,
                       3,
                       4,
                       20,
                       41,
                       43,
                       892222000
                   ],
                   "modificationDate": [
                       2025,
                       3,
                       4,
                       22,
                       12,
                       14,
                       961763000
                   ]
               },
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<any>)
        }
        return $api.get(`${this.BASE_PATH}/details`)
    }

    static async checkPermission(): Promise<AxiosResponse<any>> {
        if(use_mock){
           return  Promise.resolve({data:{},
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
           } as AxiosResponse<any>)
        }
        setTokenType("access");
        return $api.get(`${this.BASE_PATH}/check-permission`)
    }

    static registerStep1(values: ReqRegistrationStep1): Promise<AxiosResponse<any>> {
        return $api.post(`/api/v1/owners/registration/step/1`, values)
    }

    static registerStep2(values: ReqRegistrationStep2): Promise<AxiosResponse<any>> {
        return $api.post(`/api/v1/owners/registration/step/2`, values)
    }

    static async updateAddress(userId:string, req: ReqUpdateAddress) {
        return $api.put(`/api/v1/owners/${userId}/address`,req)
    }

    static async updatePersonalInformation(userId:string, req: ReqUpdatePersonalInformation) {
        return $api.put(`/api/v1/owners/${userId}/details`,req)
    }
}