import {IAddress} from "@/models/common/IAddress.ts";
import {IUser} from "@/models/common/IUser.ts";
import IOpeningHours from "@/models/common/IOpeningHours.ts";
import ICapacity from "@/models/common/ICapacity.ts";
import IParking from "@/models/common/IParking.ts";
import IError from "@/models/common/IError.ts";
import {ISessionRecord} from "@/models/common/ISessionRecord.ts";
import {IReview} from "@/models/common/IReview.ts";
import {ISessionFilter} from "@/models/common/ISessionFilter.ts";
import {ESessionStatus} from "@/models/common/ESessionStatus.ts";
import {IPage} from "@/models/common/IPage.ts";
import {statusColors} from "@/models/common/ESessionStatus.ts";
import {ISessionDetails} from "@/models/common/ISessionDetails.ts";
import {IAvgSessionsData, ISessionMetrics, IPeakHours, ITopVehiclesInPeakHours} from "@/models/common/ISessionMetrics.ts";

export type {IAddress,
    IUser,
    IOpeningHours,
    ICapacity,
    IParking,
    IError,
    ISessionRecord,
    IReview,
    ISessionFilter,
    ESessionStatus,
    IPage,
    ISessionDetails,
    IPeakHours,
    ISessionMetrics,
    ITopVehiclesInPeakHours,
    IAvgSessionsData,
}
export {
    statusColors,
}