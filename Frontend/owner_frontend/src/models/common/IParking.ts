import {IAddress} from './IAddress';
import ICapacity from "@/models/common/ICapacity.ts";
import IOpeningHours from "@/models/common/IOpeningHours.ts";



export default interface IParking {
    id: string;
    name: string;
    imageUrl: string;
    address: IAddress;
    parkingStatus: ParkingStatus;
    is24h: boolean;
    capacity: ICapacity;
    description: string;
    isPinned: boolean;
    recordStatus: string;
    ownerId: string;
    openingHours?: IOpeningHours[];
    tags: string[];
    rating: number;
}


export enum ParkingStatus {
CREATION_PROCESS = "CREATION_PROCESS",
TEMPORARY_CLOSED = "TEMPORARY_CLOSED",
    MODERATE = "ON_MODERATION",
    FULL = "FULL",
    CLOSED = "CLOSED",
    OPEN = "OPEN"
}