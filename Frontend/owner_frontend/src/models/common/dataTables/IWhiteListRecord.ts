

export interface IWhiteListRecord {
    id:string ;
    parkingId:string;
    vehicleId:string ;
    vehiclePlate:string;
    tariffId:string ;
    tariffName:string;
    createdAt:number[] | null;
    updatedAt:number[] | null;
    plateNumber?:string;
}