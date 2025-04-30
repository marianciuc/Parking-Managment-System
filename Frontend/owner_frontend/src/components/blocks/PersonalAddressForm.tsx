"use client"

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"


import { Button } from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"

import {
    InputOTP,
    InputOTPGroup,
    InputOTPSeparator,
    InputOTPSlot,
} from "@/components/ui/input-otp"
import { REGEXP_ONLY_DIGITS } from "input-otp"
import {Context} from "@/main.tsx";
import {useContext} from "react";




const formSchema = z.object({
    country: z.string().min(2, ),
    city: z.string().min(2, {
        message: "City must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_-]+$/gi, {
            message: "City can only contain letters, numbers and underscores.",
        }),
    postalCode : z.string().min(5,{
        message: "Postal Code must contain 5 digits.",
    }),
    street:z.string().min(2, {
        message: "Street must be at least 2 characters.",
    }),
    houseNumber: z.string().min(1,{
        message: "House number must be at least 1 characters.",
    }),
    apartmentNumber: z.string().optional().or(z.literal('')),
    additionalInfo: z.string().optional().or(z.literal('')),

})

export function PersonalAddressForm( ) {

    const {userStore} = useContext(Context);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            country: userStore.user.address.country,
            city: userStore.user.address.city,
            street: userStore.user.address.street,
            houseNumber:userStore.user.address.houseNumber,
            apartmentNumber:userStore.user.address.apartmentNumber,
            postalCode: userStore.user.address.postalCode,
            additionalInfo:userStore.user.address.additionalInfo,
        },
        mode: "onChange",
    })

    // 2. Define a submit handler.
    async function onSubmit(values :z.infer<typeof formSchema>) {
        try {
            await userStore.updateAddress({
                city: values.city,
                street: values.street,
                postalCode: values.postalCode,
                houseNumber: values.houseNumber,
                apartmentNumber: values.apartmentNumber ?? '',
                country: values.country,
                additionalInfo:values.additionalInfo ?? '',
            });
        } catch (error) {
            console.log(error)
        }
    }



    // const isFormValid = form.formState.isValid && form.formState.defaultValues != form.watch();


    return (
        <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 ">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="country"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Country</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your county here" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        <FormField
                            control={form.control}
                            name="city"
                            render={({ field }) => (
                                <FormItem className="text-left w-full">
                                    <FormLabel>City</FormLabel>
                                    <FormControl>
                                        <Input  placeholder="Please provide your  city" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="street"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Street</FormLabel>
                                    <FormControl>
                                        <Input   placeholder="Please provide your street" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="houseNumber"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>House Number</FormLabel>
                                    <FormControl>
                                        <Input   placeholder="Please provide your house number" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="apartmentNumber"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Apartment Number</FormLabel>
                                    <FormControl>
                                        <Input   placeholder="Please provide your apartment number" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="postalCode"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Postal Code</FormLabel>
                                    <FormControl>
                                        <InputOTP maxLength={5}{...field} pattern={REGEXP_ONLY_DIGITS} >
                                            <InputOTPGroup>
                                                <InputOTPSlot index={0} />
                                                <InputOTPSlot index={1} />
                                            </InputOTPGroup>
                                            <InputOTPSeparator />
                                            <InputOTPGroup>
                                                <InputOTPSlot index={2} />
                                                <InputOTPSlot index={3} />
                                                <InputOTPSlot index={4} />
                                            </InputOTPGroup>
                                        </InputOTP>

                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="additionalInfo"
                            render={({ field }) => (
                                <FormItem className="text-left">
                                    <FormLabel>Additional Information</FormLabel>
                                    <FormControl>
                                        <Input   placeholder="Provide additional information for your address here" {...field} />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                    </div>

                    <div className="flex items-center justify-end w-full">
                        <Button
                            type="submit"
                            className={`right-0 `}
                        >
                            Update
                        </Button>
                    </div>
                </form>
            </Form>
        </div>
    )
}
export default PersonalAddressForm;
