
export interface ITariffRecord {
    id: string | null;
    name: string;
    tariffClass : TariffClass;
    description: string;
    price: number;
    creationDate: number[] | null;
    modificationDate: number[] | null;
    recordStatus: string | null;
    version:number | null;
    parkingId: string | null ;
    currency: string | null;
}

export enum TariffClass {
    FOR_15_MINUTES = "FOR_15_MINUTES",
    FOR_30_MINUTES = "FOR_30_MINUTES",
    FOR_1_HOUR = "FOR_1_HOUR",
    FOR_WHITELISTED = "FOR_WHITELISTED",
    FOR_1_DAY = "FOR_1_DAY"
}