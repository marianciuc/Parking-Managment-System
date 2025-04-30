import {makeAutoObservable, runInAction} from "mobx";
import {IWhiteListRecord, IBlackListRecord} from "@/models/common/dataTables";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import AccessManagementService from "@/services/AccessManagementService.ts";
import {IPage} from "@/models/common";

export default class AccessManagementStore{
    page : IPage< IBlackListRecord | IWhiteListRecord>= {} as IPage<IBlackListRecord | IWhiteListRecord>;
    pageUpdate:{page: boolean} = {page: false};


    constructor(){
        makeAutoObservable(this);
    }


    async fetchAccessData(parkingId : string, isBlackList: boolean,
                          page?:number,
                          size?:number,
                          plate?:string,
                          recordId?:string,
                          vehicleId?:string, ): Promise<IToastInfo | void> {
        console.log("Fetching session data");
        try {
            const response = await AccessManagementService.fetchRecords(parkingId, isBlackList, page,size,plate,vehicleId, recordId);
            console.log(response);
            this.page = response.data;
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async addWhitelistRecord(parkingId : string, vehiclePlate: string, tariffId:string) {
        try {
            const response = await AccessManagementService.addWhitelistRecord(parkingId, vehiclePlate, tariffId);

            runInAction(()=>{
                this.page.numberOfElements+=1;
                this.page.totalElements+=1;
                if(this.page.empty){
                    this.page.empty = true;
                } else
                if(this.page.numberOfElements >= this.page.size){
                    this.page.number+=1;
                }

                this.page = this.page.numberOfElements>= this.page.size ? {...this.page, content: [response.data]}
                    : {...this.page, content: [...this.page.content, response.data]}

            })

            return {
                message: `Vehicle ${vehiclePlate} successfully added to  whitelist`,
                type: ToastType.SUCCESS,
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async addBlacklistRecord(parkingId : string, vehiclePlate: string, reason:string) {
        try {
            const response = await AccessManagementService.addBlacklistRecord(parkingId, vehiclePlate, reason);
            this.page.numberOfElements+=1;
            this.page.totalElements+=1;
            if(this.page.empty){
                this.page.empty = true;
            } else
            if(this.page.numberOfElements > this.page.size){
                this.page.number+=1;
                this.page.totalPages+=1;
            }

            this.page = this.page.numberOfElements> this.page.size ? {...this.page, content: [response.data]}
                                                                    : {...this.page, content: [...this.page.content, response.data]}

            return {
                message: `Vehicle ${vehiclePlate} successfully added to  blacklist`,
                type: ToastType.SUCCESS,
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }



    async updateWhitelistRecord(parkingId : string, vehiclePlate: string, tariffId:string) {
        try {
            await AccessManagementService.updateWhitelistRecord(parkingId, vehiclePlate, tariffId);


            // runInAction(()=>{
            //      this.page = {
            //          ...this.page, content.filter(record => record.vehiclePlate !== vehiclePlate)
            //      }
            // })

            return {
                message: `Vehicle ${vehiclePlate} successfully updated`,
                type: ToastType.SUCCESS,
            }
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async updateBlacklistRecord(parkingId : string, vehiclePlate: string, reason:string) {
        try {
            await AccessManagementService.updateBlacklistRecord(parkingId, vehiclePlate, reason);




        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async deleteWhitelistRecord(parkingId : string, whitelistId:string, vehiclePlate:string) {
        try {
            const response = await AccessManagementService.deleteWhitelistRecord(parkingId, whitelistId);

            if(response.status === 204){
                runInAction(()=>{
                    this.page.content = this.page.content.filter(record => record.vehiclePlate !== vehiclePlate  )

                })

                return {
                    message: `Vehicle ${vehiclePlate} successfully deleted from whitelist`,
                    type: ToastType.DEFAULT,
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

    async deleteBlacklistRecord(parkingId : string, blacklistId:string, vehiclePlate:string) {
        try {
            const response = await AccessManagementService.deleteBlacklistRecord(parkingId, blacklistId);

            // this.page.content.map(record => record.vehiclePlate ===  )
            if(response.status === 204){
                runInAction(()=>{
                    this.page.content = this.page.content.filter(record => record.plateNumber !== vehiclePlate  )

                })

                return {
                    message: `Vehicle ${vehiclePlate} successfully deleted from blacklist`,
                    type: ToastType.DEFAULT,
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
