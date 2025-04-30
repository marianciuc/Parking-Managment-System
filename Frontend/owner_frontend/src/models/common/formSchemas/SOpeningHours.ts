
import { z } from "zod";

export const SOpeningHours = z.object({
    is24_7: z.boolean(),
    days: z.array(
        z.object({
            // dayOfWeek: z.nativeEnum(DayOfWeek),
            openingTime: z.string().optional(), // Array of numbers for opening times
            closingTime: z.string().optional(), // Array of numbers for closing times
            isClosed: z.boolean().optional(), // Indicates if the day is closed
        })
    ),
});
