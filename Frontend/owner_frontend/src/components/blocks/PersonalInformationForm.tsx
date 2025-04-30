"use client"

import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"

import {Button} from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import {Input} from "@/components/ui/input"
import {useContext} from "react";
import {Context} from "@/main.tsx";

import { cn } from "@/lib/utils"
import { Calendar } from "@/components/ui/calendar"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import * as React from "react"
import { format } from "date-fns"
import { Calendar as CalendarIcon } from "lucide-react"





const formSchema = z.object({
    firstName: z.string().min(2, {
        message: "First name must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_]+$/gi, {
            message: "First name can only contain letters, numbers and underscores.",
        }),
    lastName: z.string().min(2, {
        message: "Last name must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_]+$/gi, {
            message: "Second name can only contain letters, numbers and underscores.",
        }),
    middleName: z.string()
        .min(2, {
            message: "Last name must be at least 2 characters.",
        })
        .regex(/^[a-zA-Z0-9_]+$/gi, {
            message: "Middle name can only contain letters, numbers and underscores.",
        })
        .optional()
        .or(z.literal(''))
    ,
    nip: z.string().min(2, {
        message: "Nip must be at least 2 characters.",
    }),
    phoneNumber: z
        .string()
        .min(9, {message: 'Must be a valid mobile number'})
        .max(14, {message: 'Must be a valid mobile number'}),
    companyName: z.string().min(2, {message: 'Company name must be at least 2 characters.'}).optional().or(z.literal('')),
    dateOfBirth: z.date({
    required_error: "A date of birth is required.",
    }).optional().or(z.literal('')),
    email: z.string().min(2, {
        message: "Email must be at least 2 characters.",
    })
        .email("Invalid email address."),
})

export function PersonalInformationForm() {
    const {userStore} = useContext(Context);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            firstName: userStore.user.firstName,
            middleName: userStore.user.middleName,
            lastName: userStore.user.lastName,
            nip: userStore.user.NIP,
            companyName: userStore.user.companyName,
            dateOfBirth: userStore.user.birthDate ? new Date(
                userStore.user.birthDate[0],
                userStore.user.birthDate[1],
                userStore.user.birthDate[2],
                userStore.user.birthDate[3],
                userStore.user.birthDate[4],
                userStore.user.birthDate[5],
                userStore.user.birthDate[6],
            ) : '',
            phoneNumber:userStore.user.phoneNumberCode? `+${userStore.user.phoneNumberCode ?? ''}${userStore.user.phoneNumber ?? ''}` : '',
            email: userStore.user.email,
        },
        mode: "onChange",
    })

    // 2. Define a submit handler.
    async function onSubmit(values: z.infer<typeof formSchema>, e?: React.BaseSyntheticEvent) {
        console.log(values);
        try {
            await userStore.updatePersonalInformation({
                firstName: values.firstName,
                middleName: values.middleName || "",
                lastName: values.lastName,
                companyName:values.companyName || "",
                dateOfBirth: values.dateOfBirth || null,
                nip: values.nip,
                phoneNumber: values.phoneNumber,
                phoneNumberCode: "",
                email:values.email,
            });
        } catch (error) {
            console.log(error)
        }
        e?.preventDefault();
        console.log(values)
    }

    // const isFormValid = form.formState.isValid;


    return (
        <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto p-6">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 relative">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="firstName"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>First Name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your first name" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="middleName"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Middle Name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your middle name" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="lastName"
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
                            name="nip"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>NIP</FormLabel>
                                    <FormControl>
                                        <Input type="number" placeholder="Please provide your NIP number" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="companyName"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Company Name</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your company name" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="dateOfBirth"
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
                                                //@ts-ignore
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
                        <FormField
                            control={form.control}
                            name="phoneNumber"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Phone number</FormLabel>
                                    <FormControl>
                                        <Input type="string"
                                               placeholder="Please provide your phone number" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="email"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Email</FormLabel>
                                    <FormControl>
                                        <Input type="string"
                                               placeholder="Please provide your email" {...field} />
                                    </FormControl>
                                    <FormMessage/>
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

export default PersonalInformationForm;
