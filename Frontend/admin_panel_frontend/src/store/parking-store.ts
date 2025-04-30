import {Page, ToastMessage, ParkingListItem, Parking} from "@/interfaces";
import {makeAutoObservable} from "mobx";
import {handleAxiosError} from "@/lib/exception-handler";
import ParkingService from "@/services/parking-service";


export default class ParkingStore {
    private currentParking: Parking | null = null;
    private parkingList: ParkingListItem[] = [];
    private currentPage: number = 0;
    private totalPages: number = 1;
    private size: number = 10;

    public get totalPagesCount(): number {
        return this.totalPages;
    }

    public get currentParkingDetails(): Parking | null {
        return this.currentParking;
    }

    public get getParkingList(): ParkingListItem[] {
        return this.parkingList;
    }

    public get currentPageNumber(): number {
        return this.currentPage;
    }

    public get sizePerPage(): number {
        return this.size;
    }

    public constructor() {
        makeAutoObservable(this);
    }

    private setParkingList(page: Page<ParkingListItem>) {
        this.currentPage = page.number;
        this.totalPages = page.size;
        this.size = page.total;
        this.parkingList = page.content;
    }

    public async searchOwners(filter: {
        page: number;
        size: number;
        id: string;
        country: string;
        city: string;
        ownerId: string;
        recordStatus: string;
        status: string;
        name: string;
    }): Promise<ToastMessage | void> {
        try {
            const res = await ParkingService.searchParking(filter);
            const data: Page<ParkingListItem> = res.data;
            this.setParkingList(data);
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


    public async fetchOwnerDetails(parkingId: string): Promise<ToastMessage | void> {
        try {
            const res = await ParkingService.fetchDriverDetails(parkingId);
            const data: Parking = res.data;
            this.currentParking = data;
            return;
        } catch (err) {
            return handleAxiosError(err);
        }
    }


}

