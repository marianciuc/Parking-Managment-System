import {IParking} from "@/models/common";
import {Controller, useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import CustomSwitch from "@/components/ui/CustomSwitch.tsx";
import {daysOfWeek} from "@/interfaces/StaticData.ts";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {SOpeningHours} from "@/models/common/formSchemas/SOpeningHours.ts";
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert.tsx";
import {Clock9} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";


function convertTimeToString(timeArray: [number,number]): string {
    const [hours, minutes] = timeArray;

    const format = (num: number): string => num.toString().padStart(2, '0');

    return `${format(hours)}:${format(minutes)}`;
}


function WorkingHours({parking}: {parking: IParking}) {



    const form = useForm<z.infer<typeof SOpeningHours>>({
        resolver: zodResolver(SOpeningHours),
        defaultValues:{
            is24_7: parking.is24h,
            days: parking.openingHours?.map((openingHour) => ({
                dayOfWeek: openingHour.dayOfWeek,
                openingTime: openingHour.openingTime? convertTimeToString(openingHour.openingTime) : "07:00:00",
                closingTime: openingHour.closingTime ? convertTimeToString(openingHour.closingTime) : "19:00:00",
                isClosed: openingHour.isClosed,
            }))

        }
    });



    function onSubmit(values : z.infer<typeof SOpeningHours>) {
        values.days.map(value =>{
            value.isClosed = !value.isClosed;
        })
        if(values.is24_7){
            console.log("24/7");
        }
        console.log(values)
    }

    return (
        <div className="w-full min-w-fit justify-center items-center mx-auto relative">

            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 flex flex-col w-full items-center">

                    <Alert className="flex flex-col px-6 py-5 bg-white shadow-lg rounded-lg border border-gray-200">
                        <div className="flex items-center mb-4">
                            <Clock9 className="h-5 w-5 text-gray-600 mr-3" />
                            <AlertTitle className="text-left text-lg font-semibold text-gray-900">
                                Parking Working Mode
                            </AlertTitle>
                        </div>
                        <AlertDescription className="text-gray-700 text-sm space-y-4">
                            <div className="flex flex-col md:flex-row justify-between items-start md:items-center">
                                {/* Текст слева */}
                                <div className="flex-1 text-left md:pr-4">
                                    <p>
                                        Your parking lot is now in 24-hour mode. Do you want to change the mode and fill in the hours manually?
                                    </p>
                                </div>

                                {/* Select справа */}
                                <div className="mt-4 md:mt-0">
                                    <Controller
                                        control={form.control}
                                        name="is24_7"
                                        render={({ field }) => (
                                            <Select
                                                onValueChange={(value) => {
                                                    field.onChange(value === "24_mode"); // true for 24-hour mode, false for manual mode
                                                }}
                                                value={field.value ? "24_mode" : "manual_mode"} // Ensure correct value mapping
                                            >
                                                <SelectTrigger className="w-[180px] rounded-md border border-gray-300 bg-gray-50 text-sm text-gray-700 py-2 px-3 shadow-sm hover:bg-gray-100 focus:ring-2 focus:ring-blue-500 focus:ring-offset-1">
                                                    <SelectValue placeholder="Choose Mode" />
                                                </SelectTrigger>
                                                <SelectContent className="bg-white rounded-md shadow-lg border border-gray-200">
                                                    <SelectItem value="manual_mode" className="text-sm text-gray-700 hover:bg-gray-100">
                                                        Manual Mode
                                                    </SelectItem>
                                                    <SelectItem value="24_mode" className="text-sm text-gray-700 hover:bg-gray-100">
                                                        24 Hours Mode
                                                    </SelectItem>
                                                </SelectContent>
                                            </Select>
                                        )}
                                    />

                                </div>
                            </div>
                        </AlertDescription>
                    </Alert>



                    <div className={`flex flex-col gap-1 justify-start items-start ${
                        form.watch("is24_7") ? "disabled-tab opacity-50" : ""
                    }`}>
                        {daysOfWeek.map((day: string) => (
                            <FormField
                                key={day}
                                control={form.control}
                                name={`days.${daysOfWeek.indexOf(day)}`}
                                render={() => (
                                    <div
                                        className=" days flex flex-row space-y-2 justify-center items-center gap-[10px]">

                                        <Controller
                                            control={form.control}
                                            name={`days.${daysOfWeek.indexOf(day)}.isClosed`}
                                            render={({field}) => (
                                                <div className="flex items-center space-x-2">
                                                    <CustomSwitch
                                                        checked={field.value}
                                                        setChecked={field.onChange}
                                                    />

                                                </div>
                                            )}
                                        />
                                        <FormLabel className="capitalize w-[100px] text-left">{day}</FormLabel>


                                        <div className={`flex
                                            ${form.watch(`days.${daysOfWeek.indexOf(day)}.isClosed`) ? '' : 'disabled-tab'} space-x-4`}>
                                            <FormField
                                                control={form.control}
                                                name={`days.${daysOfWeek.indexOf(day)}.openingTime`}
                                                render={({field}) => (
                                                    <FormItem
                                                        className="flex flex-row gap-8 items-center m-auto justify-center">
                                                        <FormLabel>From</FormLabel>
                                                        <FormControl>
                                                            <Input type="time" {...field} />
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}
                                            />

                                            <FormField
                                                control={form.control}
                                                name={`days.${daysOfWeek.indexOf(day)}.closingTime`}
                                                render={({field}) => (
                                                    <FormItem className="flex flex-row gap-8 items-center ">
                                                        <FormLabel>To</FormLabel>
                                                        <FormControl>
                                                            <Input type="time" {...field} />
                                                        </FormControl>
                                                        <FormMessage/>
                                                    </FormItem>
                                                )}
                                            />
                                        </div>

                                    </div>
                                )}
                            />
                        ))}
                    </div>
                    <div className={`flex flex-col justify-end items-end w-full`}>
                        <Button
                            type="submit"
                            className={`w - full bg-lime-600`}
                            // disabled={!isFormValid} // Disable button when the form is invalid
                        >
                            Update
                        </Button>
                    </div>

                </form>
            </Form>
        </div>
    )
}

export default WorkingHours;