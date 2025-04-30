import $api from "@/http";
import {AxiosResponse} from "axios";
import {EGateStatus, EGateType, IGate} from "@/models/common/IGate"


// const use_mock = import.meta.env.VITE_USE_MOCK;
const use_mock = true;

export default class GatesManagementService {

    static BASE_PATH = '/api/v1/gates';


    static async closeGate(parkingId:string, gateId:string): Promise<AxiosResponse<any>> {
        return $api.post(`${this.BASE_PATH}/parking/${parkingId}/gate/${gateId}/close`);
    }

    static async openGate(parkingId:string, gateId:string): Promise<AxiosResponse<any>> {
        return $api.post(`${this.BASE_PATH}/parking/${parkingId}/gate/${gateId}/open`);
    }

    static async register(req: IGate): Promise<AxiosResponse<any>> {
        return $api.post(`${this.BASE_PATH}/register/`,req);
    }

    static async unregister(gateId:string): Promise<AxiosResponse<any>> {
        return $api.post(`${this.BASE_PATH}/unregister/${gateId}`);
    }

    static async getGates(parkingId: string): Promise<AxiosResponse<IGate[]>> {
        if(use_mock){
            return Promise.resolve({
                data: [
                { id: "gate-001", name:"First", type: EGateType.IN, status: EGateStatus.OPEN, parkingId: "parking-123", host: "192.168.1.1", port: 8080, modificationDate: new Date(), isManualMode: false },
                { id: "gate-002", name:"Second", type: EGateType.OUT, status: EGateStatus.CLOSED, parkingId: "parking-123", host: "192.168.1.2", port: 8081, modificationDate: new Date(), isManualMode: true },
                { id: "gate-003", name:"Third", type: EGateType.IN, status: EGateStatus.CLOSED, parkingId: "parking-456", host: "192.168.1.3", port: 8082, modificationDate: new Date(), isManualMode: false },
                { id: "gate-004", name:"Fourth", type: EGateType.OUT, status: EGateStatus.OPEN, parkingId: "parking-456", host: "192.168.1.4", port: 8083, modificationDate: new Date(), isManualMode: true }
            ],
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IGate[]>);
        }
        return $api.get(`${this.BASE_PATH}/parking/${parkingId}`);
    }

    static async changeManualMode(parkingId:string, gateId:string, manualMode :boolean): Promise<AxiosResponse<any>> {
        return $api.post(`${this.BASE_PATH}/parking/${parkingId}/gate/${gateId}`,{
            params:{
                manualMode:manualMode,
            }
        });
    }


}
