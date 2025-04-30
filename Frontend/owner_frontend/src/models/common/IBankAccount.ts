
export enum RecordStatus{
    ACTIVE = 'ACTIVE',
    DELETED = 'DELETED',
}


export interface IBankAccount {
    id: string;
    creationDate: number[];
    modificationDate: number[];
    recordStatus: RecordStatus;
    iban:string;
    fullName: string;
    bankName: string;
    swiftCode: string;
    country: string;
}