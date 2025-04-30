import {z} from "zod";

export const SCreateTariff = z.object({
    name: z.string().min(2, {
        message: "This field is required",
    }),
    description: z.string().min(2, {
            message: "This field is required",
    }),
    class: z.string().min(2, {
        message: "This field is required",
    }),
    price: z.number().nonnegative({
        message: "This field is required",
    }),
})