/**
 * Represents a toast message used to display notifications or alerts in a user interface.
 *
 * @interface ToastMessage
 */
export default interface ToastMessage {
    message: string;
    messageType: ToastMessageType;
    statusCode: number;
}

export type ToastMessageType = "success" | "error"
