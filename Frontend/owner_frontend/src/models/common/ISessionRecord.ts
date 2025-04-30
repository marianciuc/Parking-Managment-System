
export interface ISessionRecord{
    id: string;
    parkingId: string;
    status: string;
    plateNumber: string;
    tariffName: string;
    accessList:string;
    startTime : Array<number>;
    endTime : Array<number> | undefined| null;
    paymentStatus: string;
}