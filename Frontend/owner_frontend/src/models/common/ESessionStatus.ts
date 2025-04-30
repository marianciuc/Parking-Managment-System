
export enum ESessionStatus{
    PREPARE='PREPARE',
    ACTIVE='ACTIVE',
    FINISHED='FINISHED',
    PREPARE_END='PREPARE_END',
    CANCELLED='CANCELLED',
    STOPPED_BY_OWNER='STOPPED_BY_OWNER',
    STOPPED_BY_SYSTEM='STOPPED_BY_SYSTEM',
}

export const statusColors: { [key in ESessionStatus]: string } = {
    [ESessionStatus.PREPARE]: 'text-blue-500',
    [ESessionStatus.ACTIVE]: 'text-green-600',
    [ESessionStatus.FINISHED]: 'text-gray-500',
    [ESessionStatus.PREPARE_END]: 'text-purple-500',
    [ESessionStatus.CANCELLED]: 'text-red-500',
    [ESessionStatus.STOPPED_BY_OWNER]: 'text-orange-500',
    [ESessionStatus.STOPPED_BY_SYSTEM]: 'text-yellow-500',
};
