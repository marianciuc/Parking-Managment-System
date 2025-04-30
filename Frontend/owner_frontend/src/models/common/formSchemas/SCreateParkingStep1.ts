import {z} from "zod";

export const SCreateParkingStep1 = z.object({
    name: z.string().min(2, {
        message: "Name must be at least 2 characters.",
    }),
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
    belongToInstitution: z.boolean().optional(),
    institutionName: z.string().optional(),
    termsAccept: z.boolean(),
}).refine(
    (data) => {
        // If belongToInstitution is true, institutionName must be defined and non-empty
        if (data.belongToInstitution) {
            return !!data.institutionName && data.institutionName.length >= 2;
        }
        return true;
    },
    {
        message: "Institution name is required when 'Belong to Institution' is true.",
        path: ["institutionName"], // Points to the institutionName field
    }
);