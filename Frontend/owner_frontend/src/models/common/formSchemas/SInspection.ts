import {z} from "zod";

export const SInspection = z.object({
    dob: z.date({
        required_error: "A date of inspection is required.",
    }),
    time: z.string(),
})

