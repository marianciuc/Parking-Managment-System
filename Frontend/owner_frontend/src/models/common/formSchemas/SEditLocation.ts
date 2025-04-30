

import {z} from "zod";

export const SEditLocation = z.object({
    country: z.string().min(2,),
    city: z.string().min(2, {
        message: "City must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_\-\s]+$/gi, {
            message: "City can only contain letters, numbers and underscores.",
        }),
    postalCode: z.string().min(5, {
        message: "Postal Code must contain 5 digits.",
    }),
    street: z.string().min(2, {
        message: "Street must be at least 2 characters.",
    }),
    houseNumber: z.string().min(1, {
        message: "House number must be at least 1 characters.",
    }),
    institutionName: z.string().optional(),
})