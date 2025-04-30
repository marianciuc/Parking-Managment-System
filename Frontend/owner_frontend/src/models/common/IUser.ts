export interface IUserAddress {
    country: string;
    city: string;
    street: string;
    houseNumber: string;
    postalCode: string;
    apartmentNumber: string;
    additionalInfo: string;
}



export interface IUser {
    id: string;
    userId: string;
    profilePicture: string;
    email: string;
    firstName: string;
    middleName: string;
    lastName: string;
    phoneNumber: string;
    NIP: string;
    phoneNumberCode: string;
    address: IUserAddress;
    creationDate: number[];
    isRegistrationCompleted: boolean;
    modificationDate: number[];
    isEmailVerified: boolean;
    isPhoneVerified: boolean;
    birthDate: number[];
    companyName: string;
}
