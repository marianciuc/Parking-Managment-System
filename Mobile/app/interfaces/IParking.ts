import { IAdress } from ".";

export enum ParkingStatus {
    PREPARE = "PREPARE",
    ACTIVE = "ACTIVE",
    DELETED = "DELETED",
    HIDDEN = "HIDDEN",
    DISABLED = "DISABLED",
    MODERATE = "MODERATE",
}

export default interface IParking {
    id: string;
    name: string;
    adress: IAdress;
    capacity: number;
    availaibleSpaces: number;
    isClosed: boolean;
    isFull: boolean;
    isDisabled: boolean;
    isIndoor: boolean;
    status: ParkingStatus;
}