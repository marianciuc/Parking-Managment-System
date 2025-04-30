export interface IAddress {
    id: string | undefined | null;
    countryCode: string;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
    latitude: number |null;
    longitude: number |null;
    isBelongToAnyInstitution: boolean;
    institutionName: string | undefined | null;
    recordStatus: string | undefined | null;
    creationDate: string | undefined | null;
    modificationDate: string | undefined | null;
}