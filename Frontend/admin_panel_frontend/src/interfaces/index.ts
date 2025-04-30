import AuthResponse from "@/interfaces/auth-responce";
import {AdministratorDomain} from "@/interfaces/administrator-domain";
import {OwnerDomain} from "@/interfaces/owner-domain";
import {DriverDomain} from "@/interfaces/driver-domain";
import Credentials from "@/interfaces/credentials";
import ToastMessage, {ToastMessageType} from "@/interfaces/toast-message";
import Page from "@/interfaces/page";
import UserDomain from "@/interfaces/user-domain";
import {ParkingListItem, ParkingCapacity, ParkingAddress, Parking, OpeningHours} from "@/interfaces/parking";
import {APIKey, APIKeyListElement} from "@/interfaces/api-key";
import {TagDomain} from "@/interfaces/tag-domain";

export type {
    TagDomain,
    AuthResponse,
    AdministratorDomain,
    OwnerDomain,
    DriverDomain,
    Credentials,
    ToastMessageType,
    ToastMessage,
    Page,
    UserDomain,
    ParkingListItem,
    ParkingCapacity,
    ParkingAddress,
    Parking,
    OpeningHours,
    APIKeyListElement,
    APIKey
};