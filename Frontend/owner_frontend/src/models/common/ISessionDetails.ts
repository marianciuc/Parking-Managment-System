
export interface IPayment{
    duration: number;
    totalAmount: number;
    currency : string;

}

export interface ISessionDetails {
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
}