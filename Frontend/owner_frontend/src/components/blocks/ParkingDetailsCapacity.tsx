import {IParking} from "@/models/common";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage
} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {useContext, useMemo} from "react";
import {Context} from "@/main.tsx";
import {useToast} from "@/hooks/use-toast.ts";


const formSchema = z.object({
    capacity: z.preprocess(
        (val) => (val !== null && val !== undefined && val !== '' ? Number(val) : undefined),
        z.number().min(0, {message: "Capacity must be a positive number"})
    ),
    placesForPeopleWithDisabilities: z.preprocess(
        (val) => (val !== null && val !== undefined && val !== '' ? Number(val) : undefined),
        z.number().min(0, {message: "The number of places for disabled people must be positive"})
    ),
    placesForElectricCars: z.preprocess(
        (val) => (val !== null && val !== undefined && val !== '' ? Number(val) : undefined),
        z.number().min(0, {message: "The number of places for electric cars must be positive"})
    ),
})

function ParkingDetailsCapacity({parking}: { parking: IParking }) {

    const {parkingStore} = useContext(Context);
    const {toast} = useToast();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            capacity: parking.capacity.capacity,
            placesForPeopleWithDisabilities: parking.capacity.placesForDisabled,
            placesForElectricCars: parking.capacity.placesForElectricCars,
        },
        mode: "onChange",
    })


    const isFormValid = useMemo(() => {
        // const watchedValues = form.watch();
        // console.log("Watched values:", watchedValues);

        return form.formState.isValid;
    }, [form.formState.isValid]);



    async function onSubmit() {
        try {
            const activeValues = form.getValues();

            const toastMessage = await parkingStore.updateParkingCapacity(parking.id, {
                capacity: activeValues.capacity,
                placesForDisabled: activeValues.placesForPeopleWithDisabilities,
                placesForElectricCars: activeValues.placesForElectricCars,
            });

            toast({
                title: "Capacity update",
                description: toastMessage.message,
                variant: "success"
            });
        } catch (error) {
            toast({
                title: "Error updating capacity",
                description: "Something went wrong. Please try again later.",
                variant: "destructive",
            });
        }
    }


    return (
        <div>
            <div className="flex flex-col justify-start items-start text-left gap-5 border-b-2 p-2 mb-5 label-text">
                <h3>Parking capacity</h3>
                <p> You can not change capacity to lower number that cars already parked in your spot.</p>
            </div>


            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 relative ">
                    <div className="space-y-2 flex flex-col gap-3 h-[300px]">

                        <FormField
                            control={form.control}
                            name="capacity"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Capacity</FormLabel>
                                    <FormControl>
                                        <Input type="number" step="1"
                                               placeholder="Please provide capacity of parking here" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="placesForPeopleWithDisabilities"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Places for people with disabilities</FormLabel>
                                    <FormControl>
                                        <Input type="number" step="1"
                                               placeholder="Please provide number of places for people with disabilies here or leave this field empty" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />

                        <FormField
                            control={form.control}
                            name="placesForElectricCars"
                            render={({field}) => (
                                <FormItem className="text-left w-full">
                                    <FormLabel>Places for charging electic cars</FormLabel>
                                    <FormControl>
                                        <Input type="number" step="1"
                                               placeholder="Please provide number of places for charging electic cars here or leave this field empty" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />


                        <Button
                            type="submit"
                            className={` absolute right-0 bottom-0 bg-lime-600`}
                            disabled={!isFormValid} // Disable button when the form is invalid
                        >
                            Update capacity
                        </Button>
                    </div>
                </form>
            </Form>

        </div>
    )
}

export default ParkingDetailsCapacity;