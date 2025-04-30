import {z} from "zod";


//deprecated version. use SOpening hours
export const SWorkingHours = z.object({
    is24_7: z.boolean(),
    days: z.object({
        monday: z.object({
            isClosed: z.boolean(),
            from: z.string().optional(),
            to: z.string().optional(),
        }),
        tuesday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
        wednesday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
        thursday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
        friday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
        saturday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
        sunday: z.object({ isClosed: z.boolean(), from: z.string().optional(), to: z.string().optional() }),
    }),
});