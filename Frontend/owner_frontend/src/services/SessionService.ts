import $api from "@/http"
import {ISessionFilter} from "@/models/common/ISessionFilter.ts";
import {IPage, ISessionRecord} from "@/models/common";
import {AxiosResponse} from "axios";
import { ISessionDetails} from "@/models/common/ISessionDetails.ts";

const use_mock = import.meta.env.VITE_USE_MOCK;

export default class SessionService {
    static async fetchSessions(sessionFilter: ISessionFilter, page : number, size: number,) {
        if(use_mock) {
            const sessionRecords: ISessionRecord[] = [
                { id: "1", parkingId: "P101", status: "ACTIVE", plateNumber: "BBK2906", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 10, 30,2 ,4], endTime: undefined, paymentStatus: "UNPAID" },
                { id: "2", parkingId: "P102", status: "ACTIVE", plateNumber: "KA4187AX", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 9, 15,2,2], endTime: undefined, paymentStatus: "UNPAID" },
                { id: "3", parkingId: "P103", status: "FINISHED", plateNumber: "ZS3252N", tariffName: "VIP", accessList: "WHITELIST", startTime: [2025, 4, 1, 7, 45,2,2], endTime: [2025, 4, 1, 8, 30,2,2], paymentStatus: "PAID" },
                { id: "4", parkingId: "P104", status: "ACTIVE", plateNumber: "RNI53789", tariffName: "Employee", accessList: "WHITELIST", startTime: [2025, 4, 1, 6, 30,2,2], endTime: undefined, paymentStatus: "PAID" },
                { id: "5", parkingId: "P105", status: "ACTIVE", plateNumber: "GHI654AB", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 12, 0, 12, 1 ,1 ], endTime: undefined, paymentStatus: "UNPAID" },
                { id: "6", parkingId: "P106", status: "FINISHED", plateNumber: "ZS3321A", tariffName: "VIP", accessList: "WHITELIST", startTime: [2025, 4, 1, 11, 15, 39, 1,2], endTime: [2025, 3, 1, 13, 15, 3,1, 1], paymentStatus: "PAID" },
                { id: "7", parkingId: "P107", status: "ACTIVE", plateNumber: "RST1243K", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 5, 45, 54, 12, 2], endTime: undefined,  paymentStatus: "UNPAID" },
                { id: "8", parkingId: "P108", status: "FINISHED", plateNumber: "BMW2704", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 15, 30,30, 12, 1], endTime: [2025, 3, 1, 17, 30, 2,5,2], paymentStatus: "PAID" },
                { id: "9", parkingId: "P109", status: "FINISHED", plateNumber: "AC2417VN", tariffName: "Standard", accessList: "REGULAR", startTime: [2025, 4, 1, 14, 0,29,5,1], endTime: [2025, 3, 1, 16, 0,3,3,3], paymentStatus: "PAID" },
                { id: "10", parkingId: "P110", status: "FINISHED", plateNumber: "V0233FJ", tariffName: "Employee", accessList: "WHITELIST", startTime: [2025, 4, 1, 10, 45, 2, 2, 2], endTime: [2025, 3, 1, 12, 45,3,3,3], paymentStatus: "PAID" }
            ];

            return Promise.resolve({
                data:{
                    content: sessionRecords,
                    pagable: {
                        pageNumber: page,
                        pageSize: size,
                        sort: { empty: false, sorted: true, unsorted: false },
                        offset: page * size,
                        unpaged: false,
                        paged: true
                    },
                    totalPages: 5,
                    totalElements: 10,
                    last: page === 4,
                    size,
                    number: page,
                    sort: { empty: false, sorted: true, unsorted: false },
                    numberOfElements: sessionRecords.length,
                    first: page === 0,
                    empty: sessionRecords.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<ISessionRecord>>);
        }


        return $api.get(`/api/v1/sessions/search`, {
            params: {
                page: page,
                size: size,
                sort: "creationDate",
                direction: "ASC",
                ...sessionFilter, // Spread sessionFilter to include its properties
            }
            ,

        });
    }

    static async fetchSessionDetails(sessionId: string) {
        if(use_mock){

            return Promise.resolve({
                data:{
                    id: "id",
                    durationInMinutes: 2,
                    status: "ACTIVE",
                    totalPaid: 0,
                    currency: "PLN",
                    totalAmount: 0,
                    vehiclePlateNumber: "BBK2906",
                    vehicleId: " ",
                    parkingId: " ",
                    vehicleAccessList: "REGULAR",
                    payment: {

                    },
                    tariffName: "STANDARD",
                    tariffId:"ID",

                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<ISessionDetails>);
        }
        return await $api.get(`/api/v1/sessions/${sessionId}`);
    }

    static async fetchMetrics(parkingId: string) {
        return await $api.get(`/api/v1/sessions/metrics/${parkingId}`);
    }

}