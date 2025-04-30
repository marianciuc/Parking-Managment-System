import {IError} from "@/app/interfaces";
import {AxiosError} from "axios";

export default function handleError(e: unknown, customMessage?: string, ): IError {
    const error = e instanceof AxiosError ? e : new Error('Unknown error') as AxiosError;
    console.error('Error:', error.response?.data || error.message);
    console.error('Status Code:', error.response?.status);
    return {message: customMessage || error.message, title: error.name};
}