import IPayment from "./IPayment";

export default interface ISessionList {
    id: string;
    parkingId: string;
    parkingName: string;
    status: string;
    plateNumber: string;
    tariffName: string;
    accessList: string;
    startTime: Array<number>;
    endTime: Array<number>;
    paymentStatus: string;
}