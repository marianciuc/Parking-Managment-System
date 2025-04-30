import {makeAutoObservable} from "mobx";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import { IGate} from "@/models/common/IGate.ts";
import GatesManagementService from "@/services/GatesManagementService.ts";

export default class GatesManagementStore{
    gates : IGate[] = [] as IGate[];


    constructor(){
        makeAutoObservable(this);
    }


    async fetchGatesData(parkingId : string ): Promise<IToastInfo | void> {
        console.log("Fetching gates data");
        try {
            const response = await GatesManagementService.getGates(parkingId);
            console.log(response);
            this.gates = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async closeGate(parkingId:string, gateId : string ){
        console.log("Close Gate", gateId);
        try {
            const response = await GatesManagementService.closeGate(parkingId, gateId);
            console.log(response);
            if(response.status === 200){
                this.gates = this.gates.map((element) =>
                    element.id === gateId ? response.data : element
                );
            }

        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }



    async openGate(parkingId:string, gateId : string ){
        console.log("Open Gate", gateId);
        try {
            const response = await GatesManagementService.openGate(parkingId, gateId);
            console.log(response);
            if(response.status === 200){
                this.gates = this.gates.map((element) =>
                    element.id === gateId ? response.data : element
                );
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async register(req: IGate) {
        console.log("Register Gate", req);
        try {
            const response = await GatesManagementService.register(req);
            console.log(response);
            this.gates = [...this.gates, response.data];
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async unregister(gateId : string ){
        console.log("Unregister Gate", gateId);
        try {
            const response = await GatesManagementService.unregister(gateId);
            console.log(response);
            this.gates = [...this.gates.filter((gate) => gate.id !== gateId)];
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async changeManualMode(parkingId : string, gateId: string, manualMode:boolean){
        console.log("Change Manual mode", gateId);
        try {
            const response = await GatesManagementService.changeManualMode(parkingId,gateId, manualMode);
            console.log(response);
            if(response.status === 200){
                this.gates = this.gates.map((element) =>
                    element.id === gateId ? response.data : element
                );
                return {
                    message: "Manual mode changed successfully",
                    type: ToastType.SUCCESS,
                }
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

}
