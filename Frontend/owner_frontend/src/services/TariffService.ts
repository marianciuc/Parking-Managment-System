import $api from "@/http"
import {ITariffRecord, TariffClass} from "@/models/common/ITariffRecord.ts";
import {AxiosResponse} from "axios";

const use_mock = import.meta.env.VITE_USE_MOCK;

export default class TariffService {
    static async fetchTariffs(parkingId: string) {
        if(use_mock) {
            return Promise.resolve({
                data: [
                    {
                        id: "1",
                        name: "15-Minute Tariff",
                        tariffClass: TariffClass.FOR_15_MINUTES,
                        description: "Tariff for 15 minutes",
                        price: 2.5,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P123",
                        currency: "USD",
                    },
                    {
                        id: "2",
                        name: "30-Minute Tariff",
                        tariffClass: TariffClass.FOR_30_MINUTES,
                        description: "Tariff for 30 minutes",
                        price: 4.0,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P124",
                        currency: "USD",
                    },
                    {
                        id: "3",
                        name: "1-Hour Tariff",
                        tariffClass: TariffClass.FOR_1_HOUR,
                        description: "Tariff for 1 hour",
                        price: 7.5,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P125",
                        currency: "USD",
                    },
                    {
                        id: "4",
                        name: "1-Day Tariff",
                        tariffClass: TariffClass.FOR_1_DAY,
                        description: "Tariff for 1 day",
                        price: 20.0,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P126",
                        currency: "USD",
                    },
                    {
                        id: "5",
                        name: "Employee",
                        tariffClass: TariffClass.FOR_WHITELISTED,
                        description: "Special tariff for employees",
                        price: 0.0,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P127",
                        currency: "USD",
                    },
                    {
                        id: "6",
                        name: "VIP",
                        tariffClass: TariffClass.FOR_WHITELISTED,
                        description: "Special tariff for VIP spots",
                        price: 0.0,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P128",
                        currency: "USD",
                    },
                    {
                        id: "7",
                        name: "Free",
                        tariffClass: TariffClass.FOR_WHITELISTED,
                        description: "Special tariff for free parking visitors",
                        price: 0.0,
                        creationDate: [2025, 2, 25],
                        modificationDate: null,
                        recordStatus: "ACTIVE",
                        version: 1,
                        parkingId: "P129",
                        currency: "USD",
                    },
                    // {
                    //     id: "8",
                    //     name: "Whitelist Tariff 4",
                    //     tariffClass: TariffClass.FOR_WHITELISTED,
                    //     description: "Special tariff for whitelisted users",
                    //     price: 0.0,
                    //     creationDate: [2025, 2, 25],
                    //     modificationDate: null,
                    //     recordStatus: "ACTIVE",
                    //     version: 1,
                    //     parkingId: "P130",
                    //     currency: "USD",
                    // }
                ],
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},

            } as AxiosResponse<any>)
        }
        return $api.get(`/api/v1/tariffs/parking/${parkingId}/list`);
    }

    static async createTariff(parkingId: string, tariff: ITariffRecord) {
        return $api.post(`/api/v1/tariffs/parking/${parkingId}`, tariff);
    }

    static async updateTariff(parkingId:string, tariffId:string, request: ITariffRecord) {
        return $api.put(`/api/v1/tariffs/parking/${parkingId}/${tariffId}`, request);
    }

    static async deleteTariff(parkingId:string, tariffId:string,) {
        return $api.delete(`/api/v1/tariffs/parking/${parkingId}/${tariffId}`);
    }
}