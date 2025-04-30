import $api from "@/http"
import {AxiosResponse} from "axios";
import {IBlackListRecord, IWhiteListRecord} from "@/models/common/dataTables";
import {IPage} from "@/models/common";

const use_mock = import.meta.env.VITE_USE_MOCK;

type FetchRecordsResponse<T extends boolean> = IPage<T extends true ? IBlackListRecord : IWhiteListRecord>;


export default class AccessManagementService {

    static async fetchRecords<T extends boolean>(parkingId:string , isBlackList:boolean,
                             page:number = 0,
                             size:number = 10,
                             plate?:string,
                             vehicleId?:string, recordId?:string,


    ): Promise<AxiosResponse<FetchRecordsResponse<T>>> {
        if(use_mock) {

            const content = isBlackList ? [
                { id: "1", vehicleId: "V101", plateNumber: "GH44523", reason: "Unpaid fines", createdAt: [2025, 2, 1, 12, 28, 12, 1], updatedAt: undefined },
                { id: "2", vehicleId: "V102", plateNumber: "HA9233", reason: "Unauthorized access", createdAt: [2025, 1, 10, 15, 27,46, 33], updatedAt: undefined },
                { id: "3", vehicleId: "V103", plateNumber: "BKL3491V", reason: "Dangerous driving", createdAt: [2025, 1, 5, 14, 42,33,5], updatedAt: undefined },
                { id: "4", vehicleId: "V104", plateNumber: "PZ8223J", reason: "Fraudulent activity", createdAt: [2025, 1, 15, 8, 56, 53, 32], updatedAt: undefined },
                { id: "5", vehicleId: "V105", plateNumber: "QD3339", reason: "Unauthorized access", createdAt: [2025, 2, 2, 15,41, 15, 2], updatedAt: undefined },
                { id: "6", vehicleId: "V106", plateNumber: "BFQ4414", reason: "Multiple violations", createdAt: [2025, 1, 12, 9, 15, 52, 13], updatedAt: undefined },
                { id: "7", vehicleId: "V107", plateNumber: "KZ4498", reason: "Dangerous driving", createdAt: [2025, 1, 22, 12, 4, 23, 3], updatedAt: undefined },
                { id: "8", vehicleId: "V108", plateNumber: "PL4497", reason: "Hit and run", createdAt: [2025, 1, 30, 15, 12, 53, 2], updatedAt: undefined },
                { id: "9", vehicleId: "V109", plateNumber: "BZN991N", reason: "Unauthorized access", createdAt: [2025, 2, 5, 12, 3, 4,5], updatedAt: undefined },
                { id: "10", vehicleId: "V110", plateNumber: "ROA3340", reason: "Unpaid fines", createdAt: [2025, 1, 18, 8,34, 45,22], updatedAt: undefined }
                ]
                 : [
                    { id: "101", parkingId: "P1", vehicleId: "ZS3252N", vehiclePlate: "ZS3252N", tariffId: "T1", tariffName: "VIP", createdAt: [2025, 1, 2, 12, 4,4 ], updatedAt: [2024, 2, 10, 12,2,2,2] },
                    { id: "102", parkingId: "P1", vehicleId: "V202", vehiclePlate: "RNI53789", tariffId: "T2", tariffName: "Employee", createdAt: [2025, 1, 5, 14, 22, 21, 22], updatedAt: undefined },
                    { id: "103", parkingId: "P2", vehicleId: "V203", vehiclePlate: "ZS3321A", tariffId: "T1", tariffName: "VIP", createdAt: [2025, 1, 8, 10, 2, 23,33], updatedAt: undefined },
                    { id: "104", parkingId: "P3", vehicleId: "V204", vehiclePlate: "V0233FJ", tariffId: "T3", tariffName: "Employee", createdAt: [2025, 1, 12, 13, 2, 44,2], updatedAt: undefined },
                    { id: "105", parkingId: "P1", vehicleId: "V205", vehiclePlate: "BVK223", tariffId: "T2", tariffName: "Free", createdAt: [2025, 1, 15, 7, 34 ,45, 5], updatedAt: undefined },
                    { id: "106", parkingId: "P2", vehicleId: "V206", vehiclePlate: "ZK3316JF", tariffId: "T1", tariffName: "Free", createdAt: [2025, 1, 18, 12, 41, 45, 42], updatedAt: [2025, 2, 22, 12, 2, 26,3] },
                    { id: "107", parkingId: "P3", vehicleId: "V207", vehiclePlate: "ZM33903", tariffId: "T3", tariffName: "Employee", createdAt: [2025, 1, 22, 16, 46,23,4], updatedAt: undefined },
                    { id: "108", parkingId: "P2", vehicleId: "V208", vehiclePlate: "MO433E4", tariffId: "T2", tariffName: "Free", createdAt: [2025, 1, 25, 9, 26, 53,6], updatedAt: undefined },
                    { id: "109", parkingId: "P1", vehicleId: "V209", vehiclePlate: "BN4333RS", tariffId: "T1", tariffName: "Free", createdAt: [2025, 1, 28, 10, 5,42, 3], updatedAt: undefined },
                    { id: "110", parkingId: "P3", vehicleId: "V210", vehiclePlate: "KJQ4432", tariffId: "T3", tariffName: "Free", createdAt: [2025, 2, 1, 11, 2,22,2], updatedAt: undefined }
                ];

            return Promise.resolve({
                data:{
                    content: content,
                    pagable: {
                        pageNumber: page,
                        pageSize: size,
                        sort: { empty: false, sorted: true, unsorted: false },
                        offset: page * size,
                        unpaged: false,
                        paged: true
                    },
                    totalPages: 2,
                    totalElements: 2,
                    last: page == 1,
                    size,
                    number: page,
                    sort: { empty: false, sorted: true, unsorted: false },
                    numberOfElements: content.length,
                    first: page === 0,
                    empty: content.length === 0
                },

                status: 200,
                statusText: "OK",
                headers: {},
                config: {},
            } as AxiosResponse<FetchRecordsResponse<T>>);
        }
        const list = isBlackList ? "blacklist" : "whitelist";
        return $api.get<FetchRecordsResponse<T>>(`/api/v1/parking/${parkingId}/${list}/find`,  {
            params: {
                page: page,
                size: size,
                vehiclePlate: plate,
                [isBlackList ? "blacklistId" : "whitelistId"] : recordId,
                vehicleId: vehicleId,
            }
            ,

        });
    }

    static async addWhitelistRecord(parkingId: string, vehiclePlate :string, tariffId:string) {
        return $api.post(`/api/v1/parking/${parkingId}/whitelist`, {vehiclePlate,tariffId});
    }

    static async addBlacklistRecord(parkingId: string, plateNumber :string, reason:string) {
        return $api.post(`/api/v1/parking/${parkingId}/blacklist`, {plateNumber,reason});
    }

    //@ts-ignore
    static async updateWhitelistRecord(parkingId: string, plateNumber :string, tariffId:string) {}

    //@ts-ignore
    static async updateBlacklistRecord(parkingId: string, plateNumber :string, reason:string) {}

    static async deleteWhitelistRecord(parkingId: string, whitelistId:string) {
        return $api.delete(`/api/v1/parking/${parkingId}/whitelist/${whitelistId}`);
    }

    static async deleteBlacklistRecord(parkingId: string, blacklistId:string) {
        return $api.delete(`/api/v1/parking/${parkingId}/blacklist/${blacklistId}`);
    }
}