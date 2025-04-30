

export interface ParkingListItem {
    id: string,
    name: string,
    imageUrl: string,
    address: ParkingAddress,
    capacity: number,
    occupiedSlots: number,
    recordStatus: string,
    rating: number,
    parkingStatus: string,
    is24h: boolean,
    workingTime: OpeningHours[],
    tags: string[]
}

export interface Parking {
   id: string,
    name: string,
    imageUrl: string,
    address: ParkingAddress,
    occupiedSlots: number,
    is24h: boolean,
    capacity: ParkingCapacity,
    parkingStatus: string,
    tags: string[],
    rating: number,
    recordStatus: string,
    ownerId: string,
    openingHours: OpeningHours[],
    creationDate: Date,
    modificationDate: Date,
}

export interface OpeningHours {
    openingTime: Date,
    closingTime: Date,
    dayOfWeek: string
}

export interface ParkingAddress {
    id: string,
    countryCode: string,
    city: string,
    postalCode: string,
    street: string,
    buildingNumber: string,
    latitude: string,
    longitude: string,
    isBelongToAnyInstitution: boolean,
    institutionName: string,
    recordStatus: string,
    creationDate: Date,
    modificationDate: Date
}

export interface ParkingCapacity {
    capacity: number,
    occupiedSpaces: number,
    placesForDisabled: number,
    occupiedPlacesForDisabled: number,
    placesForElectricCars: number,
    occupiedPlacesForElectricCars: number
}
