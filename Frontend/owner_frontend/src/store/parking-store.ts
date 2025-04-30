import {makeAutoObservable} from "mobx";
import {IAddress, IParking} from "@/models/common";
import ParkingService from "@/services/ParkingService.ts";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";


const ERROR_FETCHING_DATA = "An error occurred while fetching parking data.";

export default class ParkingStore {
    parkingList: IParking[];
    parking: IParking | null;

    constructor() {
        this.parkingList = [] as IParking[];
        this.parking = null;
        makeAutoObservable(this);
    }

    async fetchParkingData() {
        try {
            const res = await ParkingService.fetchParkingList();
            if (res.status === 200 && res.data != null && res.data.length > 0) {
                this.parkingList = res.data;
                console.log("parking list" + this.parkingList);
                console.log(res.data);
                return res.data;
            } else {
                this.parkingList = [];
            }
        } catch (e: unknown) {
            const error = e as Error;
            console.log(error.message);
            throw {error: error.message,};
        }
    }

    async fetchParking(id: string) {
        try {
            const res = await ParkingService.fetchParking(id);
            this.setParking(res.data);
            return res.data;
        } catch (e: unknown) {
            this.handleError(e, ERROR_FETCHING_DATA);
        }
    }

    async createParking(param: {
        name: string;
        address: {
            id: null;
            recordStatus: null;
            creationDate: null;
            modificationDate: null;
            countryCode: string;
            city: string;
            postalCode: string;
            street: string;
            buildingNumber: string;
            latitude: number | null;
            longitude: number | null;
            isBelongToAnyInstitution: boolean;
            institutionName: string | undefined
        }
    }) {
        try {
            const request = await ParkingService.createParking(param);
            this.setParking(request.data);
            this.parkingList.push(request.data);
        } catch (e: unknown) {
            const error = e as Error;
            console.log(error.message);
        }
    }

    private setParking(data: IParking) {
        if (!data?.id) {
            console.error("Error: parking ID is missing in the data.", data);
            return;
        }
        this.parking = data;
        localStorage.setItem("active-parking", data.id);
    }

    setActiveParking(details: IParking) {
        this.setParking(details);
        console.log(this.parking);
    }

    async getActiveParking() {
        const parkingId = localStorage.getItem("active-parking");
        if (!parkingId) return console.warn("Active parking ID is missing in localStorage.");
        if (this.parking?.id === parkingId) return this.parking;
        return this.fetchParking(parkingId);
    }


    async updateParkingDetails(values: {
        name: string | null;
        description: string | null;
        imageUrl: string | null;
    }): Promise<IToastInfo> {
        try {
            const res = await ParkingService.updateParking(this.parking?.id as string, values);
            this.setParking(res.data);
            return { message: "Successfully updated parking details.", type: ToastType.SUCCESS };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }


    async updateParkingLocation(values: IAddress): Promise<IToastInfo> {
        try {
            const res = await ParkingService.updateParkingLocation(this.parking?.id as string, values);
            this.setParking(res.data);
            this.parkingList.map(parking => {
                if(parking.id == res.data.id) {
                  return res.data;
                }
            })
            return { message: "Successfully updated parking details.", type: ToastType.SUCCESS };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }

    async updateParkingCapacity(id: string, data: { capacity: number; placesForDisabled: number; placesForElectricCars: number }) {
        try {
            const res = await ParkingService.updateParkingCapacity(id, data);
            if (this.parking) this.parking.capacity = res.data.capacity;
            return { message: "Successfully updated parking capacity.", type: ToastType.SUCCESS };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }


    async deleteParking(id: string) {
        try {
            await ParkingService.deleteParking(id);
            if (this.parking?.id === id) {
                this.removeActiveParking();
            }
            this.parkingList = this.parkingList.filter(parking => parking.id !== id);
            return {
                message: "Successfully deleted parking.",
                type: ToastType.SUCCESS,
            };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }

    async temporaryClose(id: string) {
        try {
            await ParkingService.temporaryClose(id);
            return {
                message: "Successfully closed parking.",
                type: ToastType.SUCCESS,
            };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }

    async temporaryOpen(id: string) {
        try {
            await ParkingService.temporaryOpen(id);
            return {
                message: "Successfully open parking.",
                type: ToastType.SUCCESS,
            };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }

    async pinParking(id: string) {
        try {
            await ParkingService.pinParking(id);
            this.parkingList.forEach(parking => {
                if (parking.id === id) {
                    parking.isPinned = true;
                }
            })
            if (this.parking?.id === id) {
                this.parking.isPinned = true;
            }
            return {
                message: "Successfully pinned parking.",
                type: ToastType.SUCCESS,
            };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }

    async unpinParking(id: string) {
        try {
            await ParkingService.unpinParking(id);
            this.parkingList.forEach(parking => {
                if (parking.id === id) {
                    parking.isPinned = false;
                }
            })
            if (this.parking?.id === id) {
                this.parking.isPinned = false;
            }
            return {
                message: "Successfully unpinned parking.",
                type: ToastType.SUCCESS,
            };
        } catch (e: unknown) {
            return this.handleError(e);
        }
    }
    private removeActiveParking() {
        this.parking = null;
        localStorage.removeItem("active-parking");
    }

    private handleError(e: unknown, customMessage?: string): IToastInfo {
        const error = e as Error;
        console.error(error.message);
        return { message: customMessage || error.message || "An error occurred", type: ToastType.ERROR };
    }

}