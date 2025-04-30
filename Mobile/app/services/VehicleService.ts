import api from "@/app/http/api"
import {IPage, IVehicle} from "@/app/interfaces";
import {AxiosResponse} from "axios";

const use_mock = false;

export default class VehicleService {
    static async getAllVehicles(userId: string, page: number, size: number) {

        if (use_mock) {
            const vehicleRecords: IVehicle[] = [
                {
                    id: "1",
                    brand: "Tesla",
                    model: "Model S",
                    hasOwner: true,
                    plateNumber: "AT3352CL",
                    color: "Red",
                    yearOfProduction: 2022,
                    placeHolder: "Electric car",
                    vin: null,
                    ownership: [],
                    creationDate: [2024, 2, 28, 10, 0],
                    modificationDate: [2025, 1, 10, 15, 30],
                    imageUrl: "https://example.com/tesla.jpg",
                },
                {
                    id: "2",
                    brand: "Renault",
                    model: "Arkana",
                    hasOwner: true,
                    plateNumber: "BBK2906",
                    color: "Gray",
                    yearOfProduction: 2021,
                    placeHolder: "SUV",
                    vin: null,
                    ownership: [],
                    creationDate: [2024, 2, 28, 11, 15],
                    modificationDate: [2025, 1, 12, 18, 45],
                    imageUrl: "https://example.com/bmw.jpg",
                },
                {
                    id: "3",
                    brand: "Toyota",
                    model: "Corolla",
                    hasOwner: true,
                    plateNumber: "SZ789IK",
                    color: "White",
                    yearOfProduction: 2019,
                    placeHolder: "Reliable sedan",
                    vin: null,
                    ownership: [],
                    creationDate: [2024, 2, 28, 12, 45],
                    modificationDate: [2025, 1, 14, 20, 0],
                    imageUrl: "https://example.com/toyota.jpg",
                },
            ];

            return Promise.resolve({
                data:{
                    content: vehicleRecords,
                    pagable: {
                        pageNumber: page,
                        pageSize: size,
                        sort: { empty: false, sorted: true, unsorted: false },
                        offset: page * size,
                        unpaged: false,
                        paged: true
                    },
                    totalPages: 5,
                    totalElements: 10,
                    last: page === 4,
                    size,
                    number: page,
                    sort: { empty: false, sorted: true, unsorted: false },
                    numberOfElements: vehicleRecords.length,
                    first: page === 0,
                    empty: vehicleRecords.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<IVehicle>>);
        }

        return await api.get(`/api/v1/vehicles/search`, {
            params: {
                page,
                size,
                ownerId: userId,
            }
            ,
        })
    }

    static async createVehicle(plateNumber: string, brand: string, model: string, assignOwnership: boolean) {
        return await api.post(`/api/v1/vehicles?assignOwnership=${assignOwnership}&plateNumber=${plateNumber}`, {
            plateNumber,
            brand,
            model
        })
    }
}