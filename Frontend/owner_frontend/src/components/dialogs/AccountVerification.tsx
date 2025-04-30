
import { Button } from "@/components/ui/button";

import {useContext} from "react";

import {Context} from "@/main.tsx";
import {zodResolver} from "@hookform/resolvers/zod";
import {SVerificationForm} from "@/models/common/formSchemas/SVerificationForm.ts";
import {Input} from "@/components/ui/input.tsx";
import {InputOTP, InputOTPGroup, InputOTPSlot} from "@/components/ui/input-otp.tsx";
import {REGEXP_ONLY_DIGITS} from "input-otp";

import { format } from "date-fns"
import { CalendarIcon } from "lucide-react"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { cn } from "@/lib/utils"
import { Calendar } from "@/components/ui/calendar"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import * as React from "react";
import PageHeader from "@/components/header/PageHeader.tsx";


// const convertToDate = (dateArray: [number, number, number]): Date => {
//     const [year, month, day] = dateArray;
//     return new Date(year, month - 1, day); // Adjust month (JS months are 0-based)
// };



function AccountVerification() {


    const { userStore } = useContext(Context);

    const user = userStore.user;
    console.log(user)

    const form = useForm<z.infer<typeof SVerificationForm>>({
        resolver: zodResolver(SVerificationForm),
        defaultValues: {
            firstname: user.firstName,
            lastname: user.lastName,
            email: user.email,
            nationalIdNumber: "",
            countryCode: user.address.country,
            city: user.address.city,
            postalCode: user.address.postalCode,
            line1 : user.address.street,
            state: "",
            //@ts-ignore
            dob : userStore.user.birthDate ? new Date(
                userStore.user.birthDate[0],
                userStore.user.birthDate[1],
                userStore.user.birthDate[2],
                userStore.user.birthDate[3],
                userStore.user.birthDate[4],
                userStore.user.birthDate[5],
                userStore.user.birthDate[6],
            ) : ''
        },
        mode: "onChange",
    });



    async function onSubmit(values: z.infer<typeof SVerificationForm>, e?: React.BaseSyntheticEvent) {
        // try {
        //     await userStore.processRegistrationStep1({
        //         // firstName: values.firstName,
        //         // middleName: values.middleName || "",
        //         // lastName: values.lastName,
        //         // NIP: values.nip,
        //         // phoneNumber: values.phone,
        //         // phoneNumberCode: values.phone.slice(0, 2),
        //     });
        // } catch (error) {
        //     console.log(error)
        // }
        e?.preventDefault();
        // // ✅ This will be type-safe and validated.
        console.log(values)
    }


    return (
    <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto">
        <PageHeader title={"Account verification"} subtitle={" Donec gravida tellus at sapien aliquet convallis. Ut euismod tortor eu turpis consectetur placerat. Praesent scelerisque vestibulum nibh, eget rutrum odio condimentum eget. "}/>
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mx-auto bg-white shadow rounded-lg p-6 max-w-[800px] flex flex-col justify-center ">
                <div className="space-y-2">
                    <FormField
                        control={form.control}
                        name="firstname"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>First Name</FormLabel>
                                <FormControl>
                                    <Input type="text" placeholder="Please provide your first name" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="lastname"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>Last Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="Please provide your last name" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="email"
                        render={({field}) => (
                            <FormItem className="text-left w-full">
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="Please provide your email" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="nationalIdNumber"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>National ID number</FormLabel>
                                <FormControl>
                                    <Input type="text" placeholder="Please provide your National ID number" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="city"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>City</FormLabel>
                                <FormControl>
                                    <Input type="text" placeholder="Please provide your city" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="postalCode"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>Postal Code</FormLabel>
                                <FormControl>
                                    <InputOTP maxLength={5}{...field}
                                              pattern={REGEXP_ONLY_DIGITS}
                                    >
                                        <InputOTPGroup>
                                            <InputOTPSlot index={0}/>
                                            <InputOTPSlot index={1}/>
                                        </InputOTPGroup>
                                        {/*<InputOTPSeparator />*/}
                                        <InputOTPGroup>
                                            <InputOTPSlot index={2}/>
                                            <InputOTPSlot index={3}/>
                                            <InputOTPSlot index={4}/>
                                        </InputOTPGroup>
                                    </InputOTP>
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="line1"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>Street</FormLabel>
                                <FormControl>
                                    <Input type="text" placeholder="Please provide your street" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="state"
                        render={({field}) => (
                            <FormItem className="text-left">
                                <FormLabel>State</FormLabel>
                                <FormControl>
                                    <Input type="text" placeholder="Please provide your city" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="dob"
                        render={({ field }) => (
                            <FormItem className="flex flex-col items-start justify-start w-full">
                                <FormLabel>Date</FormLabel>
                                <Popover>
                                    <PopoverTrigger asChild>
                                        <FormControl>
                                            <Button
                                                variant={"outline"}
                                                className={cn(
                                                    "w-full pl-3 text-left font-normal",
                                                    !field.value && "text-muted-foreground"
                                                )}
                                            >
                                                {field.value ? (
                                                    format(field.value, "PPP")
                                                ) : (
                                                    <span>Pick a date</span>
                                                )}
                                                <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                                            </Button>
                                        </FormControl>
                                    </PopoverTrigger>
                                    <PopoverContent className="w-auto p-0" align="start">
                                        <Calendar
                                            mode="single"
                                            selected={field.value}
                                            onSelect={field.onChange}
                                            disabled={(date) =>
                                                date > new Date() || date < new Date("1900-01-01")
                                            }
                                            initialFocus
                                        />
                                    </PopoverContent>
                                </Popover>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
                <Button
                    type="submit"
                    className={`self-end`}
                >
                    Verify
                </Button>
            </form>
        </Form>
    </div>)
}

export default AccountVerification;
