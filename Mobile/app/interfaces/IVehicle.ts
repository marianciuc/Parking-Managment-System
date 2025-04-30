import IOwnership from "@/app/interfaces/IOwnership";


export default interface IVehicle {
    id: string;
    brand: string;
    model: string;
    hasOwner: boolean;
    plateNumber: string;
    color: string;
    yearOfProduction: number;
    placeHolder: string;
    vin: null;
    ownership: IOwnership[];
    creationDate: Array<number>;
    modificationDate: Array<number>;
    imageUrl: string;
}