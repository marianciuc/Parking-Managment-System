import {makeAutoObservable} from "mobx";
import {ESessionStatus, ISessionFilter, ISessionRecord, ISessionDetails, ISessionMetrics, IPage} from "@/models/common";
import SessionService from "@/services/SessionService.ts";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";



export default class SessionStore {
    sessionsPage : IPage<ISessionRecord> = {} as IPage<ISessionRecord>;
    currentSession: ISessionDetails = {} as ISessionDetails;
    metrics : ISessionMetrics = {} as ISessionMetrics;

    constructor(){
        makeAutoObservable(this);
    }



    async fetchSessionsData(parkingId : string | null, page:number = 0, size: number = 10 , id? : string | null, ownerId? : string | null, plateNumber? : string | null, status?: ESessionStatus | null): Promise<IToastInfo | void> {
        console.log("Fetching session data");
        try {
            const sessionFilters: ISessionFilter = {
                parkingId : parkingId,
                id : id,
                ownerId : ownerId,
                plateNumber : plateNumber,
                status : status,
            }
            console.log(sessionFilters);
            const response = await SessionService.fetchSessions(sessionFilters,page,size);
            console.log(response);
            this.sessionsPage = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }


    async fetchSessionDetails(sessionId: string){
        console.log("Fetching session data");
        try {

            const response = await SessionService.fetchSessionDetails(sessionId);
            console.log(response);
            this.currentSession = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async fetchMetrics(parkingId: string){
        console.log("Fetching session data");
        try {

            const response = await SessionService.fetchMetrics(parkingId);
            console.log(response);
            this.metrics = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }



}