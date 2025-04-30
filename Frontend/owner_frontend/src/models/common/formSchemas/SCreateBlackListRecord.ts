import {z} from "zod";

export const SCreateBlackListRecord = z.object({
    vehiclePlateNumber: z.string().min(2, {
        message: "This field is required",
    }),
    reason: z.string().min(2, {
        message: "This field is required",
    })
})