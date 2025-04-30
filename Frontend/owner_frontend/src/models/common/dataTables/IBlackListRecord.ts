export interface IBlackListRecord {
    id: string ;
    vehicleId: string ;
    plateNumber: string;
    reason : string;
    createdAt: number[] | null;
    updatedAt: number[] | null;
    vehiclePlate?:string;
}