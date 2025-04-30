
export enum EGateType{
    IN = "IN",
    OUT = "OUT",
}
export enum EGateStatus{
    OPEN = "OPEN",
    CLOSED  = "CLOSED",
}

export interface IGate {
    id: string;
    name: string;
    type: EGateType;
    status: EGateStatus;
    parkingId:string;
    host:string;
    port:number;
    modificationDate: Date;
    isManualMode:boolean;
}