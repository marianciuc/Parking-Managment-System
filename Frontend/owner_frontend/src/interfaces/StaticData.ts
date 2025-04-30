
export const types = [
    { label: "Closed", value: "closed" },
    { label: "Open", value: "open" },
    { label: "Underground", value: "underground" },
] as const

export const security = [
    { label: "Secured", value: "secured" },
    { label: "Unsecured", value: "unsecured" },
    { label: "Video", value: "video" },
]
export interface Country {
    label: string;
    value: string;
}
 export const tabs : { label: string }[] = [
    {label:"location"},
    {label:"details"},
    {label:"time"},
    {label:"inspection"},
];



export const countries : Country[] = [
    { label: "Poland", value: "PL" },
] as const;

export const daysOfWeek = ["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];