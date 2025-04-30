import {parkingAddress, parkingCapacity, workingHours} from "@/interfaces/Interfaces.ts";


export interface CreateParking {
 name: string;
 address : parkingAddress;
}
export interface CreateParkingStep2PostData {
    entries : string;
    exits : string;
    parkingType : string;
    tags : string[];
    capacity : parkingCapacity;
    accessType : string;
}

export interface CreateParkingStep3PostData {
    is24h : boolean;
    workingHours : workingHours[];
}

export interface CreateParkingStep4PostData {
    meetingTime : [
        number,
        number,
        number,
        number,
        number,
        number,
        number,
    ]
}
export interface CreateParkingStep1Response {
    finishedStep: number;
    name: string;
    parkingId: string;
    statuses: string[];
    message: string;
    status: string;
    moderatorId : number;
}