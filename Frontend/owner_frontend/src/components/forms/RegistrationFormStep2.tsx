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

const formSchema = z.object({
    firstName: z.string().min(2, {
        message: "First name must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_\-]+$/gi, {
            message: "First name can only contain letters, numbers and underscores.",
        }),
    lastName: z.string().min(2, {
        message: "Last name must be at least 2 characters.",
    })
        .regex(/^[a-zA-Z0-9_\-]+$/gi, {
            message: "Second name can only contain letters, numbers and underscores.",
        }),
    middleName: z.string()
        .min(2, {
            message: "Last name must be at least 2 characters.",
        })
        .regex(/^[a-zA-Z0-9_\-]+$/gi, {
            message: "Middle name can only contain letters, numbers and underscores.",
        })
        .optional()
        .or(z.literal(''))
    ,
    nip: z.string().min(2, {
        message: "Nip must be at least 2 characters.",
    }),
    phone: z
        .string()
        .min(9, {message: 'Must be a valid mobile number'})
        .max(14, {message: 'Must be a valid mobile number'}),
})

export function RegistrationFormStep2({onNextStep}: { onNextStep: () => void }) {
    const {userStore} = useContext(Context);

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            firstName: "",
            lastName: "",
            middleName: "",
            nip: "",
            phone: "",
        },
        mode: "onChange",
    })

    // 2. Define a submit handler.
    async function onSubmit(values: z.infer<typeof formSchema>, e?: React.BaseSyntheticEvent) {
        try {
            await userStore.processRegistrationStep1({
                firstName: values.firstName,
                middleName: values.middleName || "",
                lastName: values.lastName,
                NIP: values.nip,
                phoneNumber: values.phone,
                phoneNumberCode: values.phone.slice(0, 2),
            });
        } catch (error) {
            console.log(error)
        }
        e?.preventDefault();
        onNextStep();
        // ✅ This will be type-safe and validated.
        console.log(values)
    }

    const isFormValid = form.formState.isValid;


    return (
        <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 ">
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
                            name="middleName"
                            render={({field}) => (
                                <FormItem className="text-left w-full">
                                    <FormLabel>Middle name (optional)</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your middle name" {...field} />
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
                            name="phone"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Phone number</FormLabel>
                                    <FormControl>
                                        <Input type="number"
                                               placeholder="Please provide your phone number" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button
                        type="submit"
                        className={`w-full ${
                            isFormValid ? "bg-neon hover:bg-neon-dark" : "bg-jordy hover:bg-gray-400"
                        }`}
                        disabled={!isFormValid}
                    >
                        Sign up
                    </Button>
                </form>
            </Form>
        </div>
    )
}

export default RegistrationFormStep2;
