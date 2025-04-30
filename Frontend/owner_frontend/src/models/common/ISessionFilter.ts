import {ESessionStatus} from "@/models/common/ESessionStatus.ts";

export interface ISessionFilter {
    id?: string | null;
    parkingId?: string | null;
    vehicleId?: string | null;
    ownerId?: string | null;
    plateNumber? : string | null;
    status?: ESessionStatus | null;
}