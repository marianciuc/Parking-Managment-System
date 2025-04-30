import {makeAutoObservable} from "mobx";
import {ITariffRecord} from "@/models/common/ITariffRecord.ts";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import TariffService from "@/services/TariffService.ts";


export default class TariffStore {
    tariffs: ITariffRecord[] = [];

    constructor() {
        makeAutoObservable(this);
    }

    async fetchTariffData(parkingId: string): Promise<IToastInfo | void> {
        try {
            const response = await TariffService.fetchTariffs(parkingId);
            this.tariffs = response.data;
            console.log(this.tariffs);
        } catch (e: unknown) {
            const error = e as Error;
            return {
                message: error.message || "An error occurred",
                type: ToastType.ERROR,
            };
        }
    }

    async createTariff(tariff: ITariffRecord, parkingId: string): Promise<IToastInfo | void> {
        try {
            const response = await TariffService.createTariff(parkingId, tariff);
            this.tariffs = this.tariffs.concat(response.data);
            return {
                message: "Tariff successfully created",
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

    async updateTariff(tariff: ITariffRecord, tariffId:string, parkingId: string): Promise<IToastInfo | void> {
        try {
            const response = await TariffService.updateTariff(parkingId, tariffId, tariff);

            if(response.status === 200){
                this.tariffs = this.tariffs.map(t =>
                    t.id === tariffId ? response.data : t
                );
                return {
                    message: "Tariff successfully updated",
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


    async deleteTariff( tariffId:string, parkingId: string): Promise<IToastInfo | void> {
        try {
            const response = await TariffService.deleteTariff(parkingId, tariffId);

            if(response.status === 200){
                this.tariffs = this.tariffs.filter(t => t.id !== tariffId);
                return {
                    message: "Tariff successfully deleted",
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