import {OwnerAddress} from "@/interfaces/owner-address";


export interface OwnerDomain {
    userId: string;
    firstName: string;
    lastName: string;
    middleName: string;
    NIP: string;
    phoneNumber: string;
    phoneCode: string;
    address: OwnerAddress;
    isRegistrationCompleted: boolean;
    birthDate: Date;
    creationDate: Date;
    modificationDate: Date;
}