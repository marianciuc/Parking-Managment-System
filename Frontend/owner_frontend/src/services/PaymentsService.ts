import $api from "@/http";
import {AxiosResponse} from "axios";
import {IAccountBalance} from "@/models/common/IAccountBalance.ts";
import { IPage } from "@/models/common";
import {ETransactionStatus} from "@/models/common/ETransactionStatus.ts";



const use_mock = import.meta.env.VITE_USE_MOCK;

export default class PaymentsService {

    static BASE_PATH = '/api/v1/transactions/stats';


    static async getAccountBalanceStats(ownerId:string): Promise<AxiosResponse<IAccountBalance>> {
        if(use_mock) {


            return Promise.resolve({
                data:{
                    currentBalance: 1283.46,
                    blockedAmount: 252.20,
                    withdrawnAmount: 2300.03,
                    currencySymbol: "PLN",
                    currency: "PLN",
                },
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IAccountBalance>);
        }

        return $api.get(`${this.BASE_PATH}/account-balance/${ownerId}`,{
            params:{
                convert:true,
            }
        });
    }

    static async getIncomesByDays(ownerId: string, from: string, to: string): Promise<AxiosResponse<any>> {
        return $api.get(`${this.BASE_PATH}/incomes/days`, {
            params:{
                ownerId:ownerId,
                convert:true,
                from:from,
                to:to,
            }
        });
    }

    static async getIncomesByDayStats(ownerId:string, from:string, to:string, parkingId?:string): Promise<AxiosResponse<any>> {
        return $api.get(`${this.BASE_PATH}/incomes/days/group-by-parking`, {
            params:{
                ownerId:ownerId,
                convert:true,
                parkingId:parkingId,
                from:from,
                to:to,
            }
        });
    }

    static async getIncomesByDaytime(ownerId:string, from:string, to:string): Promise<AxiosResponse<any>> {
        return $api.get(`${this.BASE_PATH}/incomes/days/time`, {
            params:{
                ownerId:ownerId,
                convert:true,
                from:from,
                to:to,
            }
        });
    }

    static async getIncomesByDayTimeGroupedByParking(ownerId:string, from:string, to:string, parkingId?:string): Promise<AxiosResponse<any>> {
        return $api.get(`${this.BASE_PATH}/incomes/days/time/group-by-parking`, {
            params:{
                ownerId:ownerId,
                convert:true,
                parkingId:parkingId,
                from:from,
                to:to,
            }
        });
    }

    static async getRevenue(ownerId:string, from:string, to:string, parkingId?:string): Promise<AxiosResponse<any>> {
        return $api.get(`${this.BASE_PATH}/revenue`, {
            params:{
                ownerId:ownerId,
                convert:true,
                parkingId:parkingId,
                from:from,
                to:to,
            }
        });
    }
    //@ts-ignore
    static async getPayouts(ownerId:string,
                            page:number = 0,
                            size:number = 10,
                           //@ts-ignore
                           filter?:string): Promise<AxiosResponse<IPage<any>>> {

        if(use_mock) {

            const content = [
                { id: "payout_2", status: ETransactionStatus.PENDING, dateCreated: [2025, 4, 5, 14, 24, 10, 456],bankAccount:"BANK POLSKI", amount: "120.75" },
                { id: "payout_3", status: ETransactionStatus.COMPLETED, dateCreated: [2025, 4, 5, 14, 24, 32, 789],bankAccount:"SANTANDER", amount: "890.00" },
                { id: "payout_5", status: ETransactionStatus.COMPLETED, dateCreated: [2025, 4, 5, 14, 25, 37, 345],bankAccount:"BANK POLSKI", amount: "760.90" },
                { id: "payout_6", status: ETransactionStatus.COMPLETED, dateCreated: [2025, 4, 5, 14, 26, 2, 678],bankAccount:"BANK POLSKI", amount: "450.00" },
            ];


            return Promise.resolve({
                data:{
                    content: content,
                    pagable: {
                        pageNumber: page,
                        pageSize: size,
                        sort: { empty: false, sorted: true, unsorted: false },
                        offset: page * size,
                        unpaged: false,
                        paged: true
                    },
                    totalPages: 1,
                    totalElements: 10,
                    last: true,
                    size,
                    number: page,
                    sort: { empty: false, sorted: true, unsorted: false },
                    numberOfElements: content.length,
                    first: page === 0,
                    empty: content.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<any>>);
        }
        // return $api.get(`${this.BASE_PATH}/revenue`, {
        //     params:{
        //         ownerId:ownerId,
        //         convert:true,
        //         parkingId:parkingId,
        //         from:from,
        //         to:to,
        //     }
        // });
    }

    //@ts-ignore
    static async getIncoming(ownerId:string,
                            page:number = 0,
                            size:number = 10,
                            //@ts-ignore
                            filter?:string): Promise<AxiosResponse<IPage<any>>> {

        if(use_mock) {

            const content = [
                { id: "1", status: "COMPLETED", method: "blik", amount: "12.21", description: "Parking 1" },
                { id: "2", status: "PENDING", method: "google pay", amount: "7.50", description: "Parking 1" },
                { id: "3", status: "COMPLETED", method: "blik", amount: "45.00", description: "Parking 2" },
                { id: "4", status: "COMPLETED", method: "apple pay", amount: "30.02", description: "Parking 1" },
                { id: "5", status: "PENDING", method: "card", amount: "10.80", description: "Parking 1" },
                { id: "6", status: "COMPLETED", method: "blik", amount: "10.75", description: "Parking 2" },
                { id: "7", status: "COMPLETED", method: "blik", amount: "35.00", description: "Parking 1" },
                { id: "8", status: "COMPLETED", method: "apple pay", amount: "19,76", description: "Parking 2" },
                { id: "9", status: "PENDING", method: "blik", amount: "23.05", description: "Parking 1" },
                { id: "10", status: "COMPLETED", method: "blik", amount: "9.78", description: "Parking 1" }

            ];


            return Promise.resolve({
                data:{
                    content: content,
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
                    numberOfElements: content.length,
                    first: page === 0,
                    empty: content.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<any>>);
        }
        // return $api.get(`${this.BASE_PATH}/revenue`, {
        //     params:{
        //         ownerId:ownerId,
        //         convert:true,
        //         parkingId:parkingId,
        //         from:from,
        //         to:to,
        //     }
        // });
    }


}
