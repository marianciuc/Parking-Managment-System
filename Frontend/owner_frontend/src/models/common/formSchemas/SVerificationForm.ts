import {z} from "zod";

export const SVerificationForm = z.object({
    firstname: z.string().min(2, {
        message: "Name must be at least 2 characters.",
    }),
    lastname: z.string().min(2, {
        message: "Name must be at least 2 characters.",
    }),
    email: z
        .string()
        .min(2, { message: "Email must be at least 2 characters.", })
        .email("Invalid email address"),
    nationalIdNumber: z.string().regex(/^[0-9\-\s]+$/gi, {
        message: "National ID must be numeric.",
    }),
    countryCode: z.string().regex(/^[A-Z]{2}$/, "Invalid country code"),
    city: z.string().min(2, {
        message: "City must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_\-\s]+$/gi, {
            message: "City can only contain letters, numbers and underscores.",
        }),
    postalCode: z.string().min(5, {
        message: "Postal Code must contain 5 digits.",
    }),
    line1: z.string().min(2, {
        message: "Street must be at least 2 characters.",
    }),
    state: z.string().min(2, {
        message: "State must be at least 2 characters.",
    }),
    dob: z.date({
        required_error: "A date of birth is required.",
    }),

})