import {ToastMessageType, ToastMessage} from "@/interfaces";
import {AxiosError} from "axios";

export function handleAxiosError(error: unknown): ToastMessage {
    const axiosError = error as AxiosError;
    return <ToastMessage>{
        messageType: "error" as ToastMessageType,
        message: axiosError.message,
        statusCode: axiosError.status,
    };
}
