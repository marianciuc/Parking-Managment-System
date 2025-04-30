import api from "@/app/http/api";
import {ISessionList} from "../interfaces";
import {IPage} from "@/app/interfaces";
import {AxiosResponse} from "axios";

const use_mock = process.env.EXPO_PUBLIC_USE_MOCK;

export default class SessionService {

    static async fetchSessions(userId: string, page: number, size: number) {
        console.log("Use mock state: ", use_mock);
        if (use_mock) {
            const sessionRecords: ISessionList[] = [
                {
                    id: "1",
                    parkingId: "P101",
                    status: "active",
                    plateNumber: "ABC-123",
                    tariffName: "Standard",
                    accessList: "Gate1, Gate2",
                    startTime: [2025, 3, 1, 8, 30],
                    endTime: [2025, 3, 1, 10, 30],
                    paymentStatus: "paid"
                },
                {
                    id: "2",
                    parkingId: "P102",
                    status: "inactive",
                    plateNumber: "XYZ-789",
                    tariffName: "Premium",
                    accessList: "Gate3",
                    startTime: [2025, 3, 1, 9, 15],
                    endTime: [2025, 3, 1, 11, 15],
                    paymentStatus: "unpaid"
                },
                {
                    id: "3",
                    parkingId: "P103",
                    status: "active",
                    plateNumber: "LMN-456",
                    tariffName: "Economy",
                    accessList: "Gate1, Gate4",
                    startTime: [2025, 3, 1, 7, 45],
                    endTime: [2025, 3, 1, 9, 45],
                    paymentStatus: "pending"
                },
                {
                    id: "4",
                    parkingId: "P104",
                    status: "completed",
                    plateNumber: "DEF-321",
                    tariffName: "Standard",
                    accessList: "Gate2",
                    startTime: [2025, 3, 1, 6, 30],
                    endTime: [2025, 3, 1, 8, 30],
                    paymentStatus: "paid"
                },
                {
                    id: "5",
                    parkingId: "P105",
                    status: "active",
                    plateNumber: "GHI-654",
                    tariffName: "VIP",
                    accessList: "Gate1, Gate3",
                    startTime: [2025, 3, 1, 12, 0],
                    endTime: [2025, 3, 1, 14, 0],
                    paymentStatus: "paid"
                },
                {
                    id: "6",
                    parkingId: "P106",
                    status: "inactive",
                    plateNumber: "JKL-987",
                    tariffName: "Economy",
                    accessList: "Gate4",
                    startTime: [2025, 3, 1, 11, 15],
                    endTime: [2025, 3, 1, 13, 15],
                    paymentStatus: "unpaid"
                },
                {
                    id: "7",
                    parkingId: "P107",
                    status: "completed",
                    plateNumber: "MNO-741",
                    tariffName: "Standard",
                    accessList: "Gate2, Gate3",
                    startTime: [2025, 3, 1, 5, 45],
                    endTime: [2025, 3, 1, 7, 45],
                    paymentStatus: "paid"
                },
                {
                    id: "8",
                    parkingId: "P108",
                    status: "active",
                    plateNumber: "PQR-852",
                    tariffName: "Premium",
                    accessList: "Gate1",
                    startTime: [2025, 3, 1, 15, 30],
                    endTime: [2025, 3, 1, 17, 30],
                    paymentStatus: "pending"
                },
                {
                    id: "9",
                    parkingId: "P109",
                    status: "inactive",
                    plateNumber: "STU-963",
                    tariffName: "Economy",
                    accessList: "Gate3, Gate4",
                    startTime: [2025, 3, 1, 14, 0],
                    endTime: [2025, 3, 1, 16, 0],
                    paymentStatus: "unpaid"
                },
                {
                    id: "10",
                    parkingId: "P110",
                    status: "completed",
                    plateNumber: "VWX-159",
                    tariffName: "VIP",
                    accessList: "Gate2",
                    startTime: [2025, 3, 1, 10, 45],
                    endTime: [2025, 3, 1, 12, 45],
                    paymentStatus: "paid"
                }
            ];

            return Promise.resolve({
                data:{
                    content: sessionRecords,
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
                    numberOfElements: sessionRecords.length,
                    first: page === 0,
                    empty: sessionRecords.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<IPage<ISessionList>>);
        }


            return api.get(`/api/v1/sessions/search`, {
                params: {
                    page,
                    size,
                    sort: "creationDate",
                    direction: "desc",
                    ownerId: userId,
                }
                ,
            });
        }

    }

