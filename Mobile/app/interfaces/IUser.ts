export default interface IUser {
    userId: string;
    profilePicture: string;
    firstName: string;
    lastName: string;
    country: string;
    city: string;
    email: string;
    phoneNumber: string;
    phoneCode: string;
    birthDate: Date;
    isEmailVerified: boolean;
    isPhoneVerified: boolean;
};
