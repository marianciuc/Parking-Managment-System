
export default interface UserDomain {
    id: string;
    email: string;
    creationDate: Date;
    lastModifiedDate: Date;
    recordStatus: string;
    possibility: string[];
    userType: UserType;
}

export enum UserType {
    ADMIN = "ADMIN",
    DRIVER = "DRIVER",
    OWNER = "PARKING_OWNER"
}
