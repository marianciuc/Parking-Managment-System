import $api from "@/http"
import {AxiosResponse} from "axios";
import {CreateParkingRequest} from "@/models/requests";
import {IAddress, IParking} from "@/models/common";

interface ParkingDetailsUpdateReq {
    name: string | null;
    description: string | null;
    imageUrl: string | null;
}

interface ParkingCapacityUpdateReq {
    capacity: number;
    placesForDisabled: number;
    placesForElectricCars: number;
}

const use_mock = import.meta.env.VITE_USE_MOCK;

export default class ParkingService {
    static async createParking(values : CreateParkingRequest ) : Promise<AxiosResponse<IParking>> {
        return $api.post<IParking>(`/api/v1/parking/creation`, values);
    }

    static async fetchParkingList(): Promise<AxiosResponse<IParking[]>> {
        console.log('Use mock',use_mock);
        if(use_mock){
            console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
            return Promise.resolve({
                data: [
                    {
                        id: "555fd032-eabb-47e8-8005-d2f27c1b0074",
                        name: "Parking 1",
                        imageUrl: null,
                        address: {
                            id: "38b16655-70bd-439d-8269-90105174023f",
                            countryCode: "PL",
                            city: "Szczecin",
                            postalCode: "71450",
                            street: "Chopina",
                            buildingNumber: "55",
                            latitude: 53.4525613,
                            longitude: 14.5383154,
                            isBelongToAnyInstitution: false,
                            institutionName: "",
                            version: 0,
                            recordStatus: "ACTIVE",
                            creationDate: "2025-02-25T13:19:44.684763Z",
                            modificationDate: "2025-02-25T13:19:44.684763Z",
                        },
                        parkingStatus: "OPEN",
                        description: null,
                        is24h: true,
                        capacity: {
                            capacity: 60,
                            occupiedSpaces: 23,
                            placesForDisabled: 0,
                            occupiedPlacesForDisabled: 0,
                            placesForElectricCars: 0,
                            occupiedPlacesForElectricCars: 0,
                        },
                        isPinned: false,
                        rating: 0.0,
                        recordStatus: "ACTIVE",
                        ownerId: "eb233a59-6f1a-4616-b698-c5a853f154bb",
                        openingHours: [
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "MONDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "TUESDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "WEDNESDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "THURSDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "FRIDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "SATURDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "SUNDAY" },
                        ],
                    },
                    {
                        id: "555fd032-feabb-47e8-8005-d2f27c1b0074",
                        name: "Parking 2",
                        imageUrl: null,
                        address: {
                            id: "38b16655-70bd-439d-8269-90105174023f",
                            countryCode: "PL",
                            city: "Szczecin",
                            postalCode: "71450",
                            street: "Chopina",
                            buildingNumber: "53",
                            latitude: 53.4525613,
                            longitude: 14.5383154,
                            isBelongToAnyInstitution: false,
                            institutionName: "",
                            version: 0,
                            recordStatus: "ACTIVE",
                            creationDate: "2025-02-25T13:19:44.684763Z",
                            modificationDate: "2025-02-25T13:19:44.684763Z",
                        },
                        parkingStatus: "OPEN",
                        description: null,
                        is24h: true,
                        capacity: {
                            capacity: 40,
                            occupiedSpaces: 15,
                            placesForDisabled: 0,
                            occupiedPlacesForDisabled: 0,
                            placesForElectricCars: 0,
                            occupiedPlacesForElectricCars: 0,
                        },
                        isPinned: false,
                        rating: 0.0,
                        recordStatus: "ACTIVE",
                        ownerId: "eb233a59-6f1ad-4616-b698-c5a853f154bb",
                        openingHours: [
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "MONDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "TUESDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "WEDNESDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "THURSDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "FRIDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "SATURDAY" },
                            { isClosed: false, openingTime: [0, 0], closingTime: [23, 59], dayOfWeek: "SUNDAY" },
                        ],
                    },
                ],
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<any>);
        }
        return $api.get(`/api/v1/parking`);
    }

    static async fetchParking(id: string) {
        if(use_mock){
            return Promise.resolve({
                data:{
                    "id": "555fd032-eabb-47e8-8005-d2f27c1b0074",
                    "name": "Parking 1",
                    "imageUrl": null,
                    "address": {
                        "id": "38b16655-70bd-439d-8269-90105174023f",
                        "countryCode": "PL",
                        "city": "Szczecin",
                        "postalCode": "71450",
                        "street": "Chopina",
                        "buildingNumber": "55",
                        "latitude": 53.4525613,
                        "longitude": 14.5383154,
                        "isBelongToAnyInstitution": false,
                        "institutionName": "",
                        "version": 0,
                        "recordStatus": "ACTIVE",
                        "creationDate": [
                            2025,
                            2,
                            25,
                            13,
                            19,
                            44,
                            684763000
                        ],
                        "modificationDate": [
                            2025,
                            2,
                            25,
                            13,
                            19,
                            44,
                            684763000
                        ]
                    },
                    "parkingStatus": "CREATION_PROCESS",
                    "description": null,
                    "is24h": true,
                    "capacity": {
                        "capacity": 60,
                        "occupiedSpaces": 23,
                        "placesForDisabled": 0,
                        "occupiedPlacesForDisabled": 0,
                        "placesForElectricCars": 0,
                        "occupiedPlacesForElectricCars": 0
                    },
                    "isPinned": false,
                    "rating": 0.0,
                    "recordStatus": "ACTIVE",
                    "ownerId": "eb233a59-6f1a-4616-b698-c5a853f154bb",
                    "openingHours": [
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "MONDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "TUESDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "WEDNESDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "THURSDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "FRIDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "SATURDAY"
                        },
                        {
                            "isClosed": false,
                            "openingTime": [
                                0,
                                0
                            ],
                            "closingTime": [
                                23,
                                59
                            ],
                            "dayOfWeek": "SUNDAY"
                        }
                    ]
                },
                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<any>);
        }
        return $api.get(`/api/v1/parking/${id}`);
    }

    static async updateParking(id: string, values: ParkingDetailsUpdateReq) {
        console.log(values)
        return $api.put(`/api/v1/parking/${id}`, values);
    }

    static async updateParkingLocation(id: string, values: IAddress){
        return $api.put(`/api/v1/parking/${id}/address`, values);
    }

    static async temporaryClose(id: string) {
        return $api.put(`/api/v1/parking/${id}/status/temporary-close`);
    }

    static async temporaryOpen(id: string) {
        return $api.put(`/api/v1/parking/${id}/status/open`);
    }

    static async updateParkingCapacity(id: string, values: ParkingCapacityUpdateReq) {
        return $api.put(`/api/v1/parking/${id}/capacity`, values);
    }

    static async deleteParking(id: string) {
        return $api.delete(`/api/v1/parking/${id}`);
    }

    static async pinParking(id: string) {
        return $api.put(`/api/v1/parking/${id}/pin`);
    }

    static async unpinParking(id: string) {
        return $api.put(`/api/v1/parking/${id}/unpin`);
    }

}