
export interface IAvgSessionsData{
    vehicleId: string;
    plateNumber: string;
    avgSessionsPerMonth: number;
}


export interface IPeakHours{
    dayOfWeek: number;
    hourOfDay: number;
    activeSessions: number;
}

export interface ITopVehiclesInPeakHours{
    vehicleId: string;
    plateNumber: string;
    sessionCount: number;
}



export interface ISessionMetrics {
    activeSessions: number;
    averageTimeOfActiveSessions: number;
    totalSessionsForWeek: number;
    totalSessionsForMonth: number;
    totalSessionsForYear: number;
    totalSessionsForAllTime: number;
    personalTariffProposition: IAvgSessionsData[];
    peakHours : IPeakHours[];
    topVehiclesInPeakHours : ITopVehiclesInPeakHours[];
}