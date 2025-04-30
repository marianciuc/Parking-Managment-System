import {z} from "zod";

export const SCreateParkingStep2 = z.object({

    capacity: z.preprocess((val) => Number(val), z.number().positive("Capacity must be a positive number")),
    entries: z.preprocess((val) => Number(val), z.number().int().positive("Entries must be a positive integer")),
    exits: z.preprocess((val) => Number(val), z.number().int().positive("Exits must be a positive integer")),
    type: z.string().nonempty("Type of parking is required"),
    security: z.string().nonempty("Security is required"),
    evChargingStations: z.string().optional(),
    placesForPeopleWithDisabilities: z.string().optional(),
    heating: z.boolean().optional(),
    bicycleRack: z.boolean().optional(),

})