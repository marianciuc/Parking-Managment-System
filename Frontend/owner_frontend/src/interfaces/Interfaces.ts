


export enum RegistrationStatus {
    Registred,
    onModeration,
    Unregistered,
}
export enum OpenStatus {
    Open,
    Closed,
    Unregistered,
}
export enum ParkingType {
    Open,
    Closed,
    Underground
}

 export interface Parking {
    // className : string;
    parkingName : string;
    parkingAddress : string;
    slots : number;
    occupiedSlots : number;
    registrationStatus : RegistrationStatus;
    isOpen: OpenStatus;
    isPinned:boolean;
}
 export interface ParkingDetails {
    name: string ;
    location: string;
    capacity : number;
    entries : number;
    exits : number;
    type : ParkingType;
    placesForPeopleWithDisabilities : number;
    evChargingStation : number;
    heating : boolean;
    workingTime : string;
}

export interface ParkingDetailsEdit{
    name : string | undefined ;
    capacity : number | undefined;
    entries : number | undefined;
    exits : number | undefined;
    type : string | undefined;
    security: string | undefined;
    placesForPeopleWithDisabilities : number | undefined;
    evChargingStation : number | undefined;
    heating : boolean | undefined;
    bicycleRack : boolean | undefined;
    is24_7 : boolean | undefined;
    days : days | undefined;
}

export interface  parkingAddress {
    countryCode : string;
    city : string;
    postalCode : string;
    street: string;
    buildingNumber : string;
    latitude : number;
    longitude : number;
    isBelongToAnyInstitution : boolean
    institutionName : string;
}

export interface parkingCapacity {
    capacity : number;
    placesForDisabled : number;
    placesForElectricCars : number
}

export interface workingHours {
    isClosed : boolean;
    openingTime : string;
    closingTime : string;
    dayOfWeek : number;
}


export interface CreateParkingStep1Values{
    name : string | undefined;
    country: string | undefined;
    city : string | undefined;
    postalCode:string | undefined;
    street : string | undefined;
    houseNumber : string | undefined;
    belongToInstitution?: boolean;
    institutionName ?: string | undefined;
    termsAccept:boolean | undefined;
}


export interface ClosingTime {
    isClosed: boolean | undefined;
    from?:string | undefined;
    to?: string | undefined;
}


export interface days {
    monday : ClosingTime | undefined;
    tuesday : ClosingTime | undefined;
    wednesday : ClosingTime | undefined;
    thursday : ClosingTime | undefined;
    friday : ClosingTime | undefined;
    saturday: ClosingTime | undefined;
    sunday: ClosingTime | undefined;
}

