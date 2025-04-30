import IVehicle from "@/app/interfaces/IVehicle";
import {makeAutoObservable} from "mobx";
import {IPage} from "@/app/interfaces";
import handleError from "@/app/utils/ErrorHandler";
import VehicleService from "@/app/services/VehicleService";
import {AxiosError} from "axios";

export class VehicleStore {
    page: number = 0;
    pageSize: number = 20;
    vehiclePage: IVehicle[] = [];
    currentVehicle: IVehicle[] | null = null;
    totalPages: number = 0;

    constructor() {
        makeAutoObservable(this);
    }

    setVehiclePage(vehiclePage: IPage<IVehicle>) {
        this.vehiclePage = vehiclePage.content;
        this.totalPages = vehiclePage.totalPages;
        this.page = vehiclePage.number;
    }

    fetchVehicle = async (userId: string, page: number) => {
        try {
            const request = await VehicleService.getAllVehicles(userId, page, this.pageSize,);
            if (request.data) {
                const vehicleResponse: IPage<IVehicle> = request.data;
                this.setVehiclePage(vehicleResponse);
                console.log("vehicleResponse", vehicleResponse);
            } else {
                console.log("unexpected message (vehicleStore)");
                throw new AxiosError("unexpected message (vehicleStore)");

            }
        } catch (e: unknown) {
            handleError(e, "error fetching vehicle");
        }
    }
}

