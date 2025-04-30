import { IAddress} from "@/models/common";

export default interface CreateParkingRequest {
    name: string;
    address: IAddress;
}