export interface ReqRegistrationStep1 {
    firstName: string;
    middleName: string;
    lastName: string;
    NIP: string;
    phoneNumber: string;
    phoneNumberCode: string;
}

export interface ReqRegistrationStep2 {
    city: string;
    street: string;
    postalCode: string;
    houseNumber: string;
    apartmentNumber: string;
    country: string;
}

export interface ReqUpdateAddress{
    country: string;
    city: string;
    street: string;
    houseNumber: string;
    apartmentNumber: string;
    postalCode: string;
    additionalInfo: string;
}

export interface ReqUpdatePersonalInformation {
    firstName: string;
    middleName: string;
    lastName: string;
    nip: string;
    companyName: string;
    dateOfBirth: Date | null | undefined;
    phoneNumber: string;
    phoneNumberCode: string;
    email: string;
}