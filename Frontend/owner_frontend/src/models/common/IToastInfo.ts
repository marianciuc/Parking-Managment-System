export type IToastInfo = {
    message: string;
    type: ToastType;
}

export enum ToastType {
    SUCCESS = 'success',
    ERROR = 'error',
    WARNING = 'warning',
    INFO = 'info',
    DEFAULT = 'default',
    DESTRUCTIVE = 'destructive'
}