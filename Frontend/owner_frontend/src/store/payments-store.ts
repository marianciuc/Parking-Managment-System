import { makeAutoObservable, runInAction} from "mobx";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import PaymentsService from "@/services/PaymentsService.ts";
import {IAccountBalance} from "@/models/common/IAccountBalance.ts";
import {IPage} from "@/models/common";


function formatDate(date: Date) {
    return date.toISOString().replace("Z", "");
}
function dateToTimestampArray(date: Date): number[] {
    return [
        date.getFullYear(),      // Year
        date.getMonth()+1,         // Month (0-11)
        date.getDate(),          // Day of the month (1-31)
        date.getHours(),         // Hour (0-23)
        date.getMinutes(),       // Minute (0-59)
        date.getSeconds(),       // Second (0-59)
        date.getMilliseconds()   // Millisecond (0-999)
    ];
}

export default class PaymentsStore {
    accountBalanceStats : IAccountBalance = {} as IAccountBalance;
    incomesByDays = {};
    incomesByDayStats = {};
    incomesByDaytime  = {};
    incomesByDayTimeGroupedByParking = {};
    revenue = {};
    payouts : IPage<any>={} as IPage<any>;
    incoming : IPage<any>={} as IPage<any>;

    constructor(){
        makeAutoObservable(this);
    }

    async getAccountBalanceStats(ownerId : string): Promise<IToastInfo | void> {
        console.log("Fetching account balance stats  data");
        try {
            const response = await PaymentsService.getAccountBalanceStats(ownerId);
            console.log(response);
            if(response.status === 200){
                this.accountBalanceStats = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async getIncomesByDays(ownerId : string,
                               from:Date,
                               to:Date): Promise<IToastInfo | void> {
        console.log("Fetching Incomes by days  data");
        try {
            const response = await PaymentsService.getIncomesByDays(ownerId, formatDate(from), formatDate(to));
            console.log(response);
            if(response.status === 200){
                this.incomesByDays = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async getIncomesByDayStats(ownerId : string,
                                              from:Date,
                                              to:Date,
                                              parkingId?:string,): Promise<IToastInfo | void> {
        console.log("Fetching Incomes by day stats  data");
        try {
            const response = await PaymentsService.getIncomesByDayStats(ownerId, formatDate(from), formatDate(to),parkingId );
            console.log(response);
            if(response.status === 200){
                this.incomesByDayStats = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async getIncomesByDayTime(ownerId : string,
                                              from:Date,
                                              to:Date ): Promise<IToastInfo | void> {
        console.log("Fetching Incomes by day time  data");
        try {
            const response = await PaymentsService.getIncomesByDaytime(ownerId, formatDate(from), formatDate(to) );
            console.log(response);
            if(response.status === 200){
                this.incomesByDaytime = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }


    async getIncomesByDayTimeGroupedByParking(ownerId : string,
                                              from:Date,
                                              to:Date,
                                              parkingId?:string,): Promise<IToastInfo | void> {
        console.log("Fetching Incomes by day time grouped by parking data");
        try {
            const response = await PaymentsService.getIncomesByDayTimeGroupedByParking(ownerId, formatDate(from), formatDate(to),parkingId );
            console.log(response);
            if(response.status === 200){
                this.incomesByDayTimeGroupedByParking = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }


    async getRevenue(ownerId : string,
                       from:Date,
                       to:Date,
                       parkingId?:string,): Promise<IToastInfo | void> {
        console.log("Fetching revenue data");
        try {
            const response = await PaymentsService.getRevenue(ownerId, formatDate(from), formatDate(to),parkingId );
            console.log(response);
            if(response.status === 200){
                this.revenue = response.data;
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async getPayouts(ownerId : string,
                     page?:number,
                     size?:number,
                     filter?:string): Promise<IToastInfo | void> {
        console.log("Fetching revenue data");
        try {
            const response = await PaymentsService.getPayouts(ownerId, page, size, filter );
            console.log(response);
            if(response.status === 200){
                runInAction(()=>{
                    this.payouts = response.data;
                })
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }



    async requestPayout(ownerId : string, amount:number, bankAccount:string){
        const newPayout = {id:1, status:"PENDING" ,dateCreated: dateToTimestampArray(new Date()) , bankAccount: bankAccount, method:"payout", amount:amount};
        console.log("New payout",newPayout, ownerId);
        runInAction(() =>{
            this.payouts.content  = [newPayout, ...this.payouts.content];
            this.payouts = {...this.payouts}
            this.payouts.numberOfElements +=1;
        })
        console.log(this.payouts.content);
    }

    async getIncoming(ownerId : string,
                     page?:number,
                     size?:number,
                     filter?:string): Promise<IToastInfo | void> {
        console.log("Fetching revenue data");
        try {
            const response = await PaymentsService.getIncoming(ownerId, page, size, filter );
            console.log(response);
            if(response.status === 200){
                this.incoming = response.data;
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
