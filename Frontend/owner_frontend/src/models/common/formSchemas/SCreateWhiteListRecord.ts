import {z} from "zod";

export const SCreateWhiteListRecord = z.object({
    vehiclePlateNumber: z.string().min(2, {
        message: "This field is required",
    }),
    tariff: z.string(),
})