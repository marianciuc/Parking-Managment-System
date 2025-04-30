export default interface IOpeningHours {
    dayOfWeek: DayOfWeek;
    openingTime?: [number, number];
    closingTime?: [number, number];
    isClosed?: boolean;
}

export enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}