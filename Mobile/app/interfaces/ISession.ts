import IPayment from "@/app/interfaces/IPayment";

export enum SessionStatus {
        PREPARE = "PREPARE",
        ACTIVE = "ACTIVE",
        FINISHED = "FINISHED",
        PREPARE_END = "PREPARE_END",
        CANCELLED = "CANCELLED",
        STOPPED_BY_OWNER = "STOPPED_BY_OWNER",
        STOPPED_BY_SYSTEM = "STOPPED_BY_SYSTEM",
}

export default interface ISession {
    id: string;
    durationInMinutes: number;
    status: string;
    totalPaid: number;
    currency: string;
    totalAmount: number;
    vehiclePlateNumber: string;
    vehicleId: string;
    parkingId: string;
    vehicleAccessList: string;
    payment: IPayment;
    tariffName: string;
    tariffId: string;
};
