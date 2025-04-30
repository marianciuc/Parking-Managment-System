
import { Button } from "@/components/ui/button";
import {useContext,} from "react";

import {Context} from "@/main.tsx";
import {zodResolver} from "@hookform/resolvers/zod";
import {Input} from "@/components/ui/input.tsx";

import { Check, ChevronsUpDown} from "lucide-react"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { cn } from "@/lib/utils"
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
import {SAddNewBankAccountForm} from "@/models/common/formSchemas/SAddNewBankAccountForm.ts";
import {countries} from "@/interfaces/StaticData.ts";
import {Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";





function AddNewBankAccount() {


    const { userStore } = useContext(Context);

    const user = userStore.user;
    console.log(user)

    const form = useForm<z.infer<typeof SAddNewBankAccountForm>>({
        resolver: zodResolver(SAddNewBankAccountForm),
        defaultValues: {
            fullname: user.firstName + ' ' + user.lastName,
            iban: '',
            bankName: '',
            swiftCode: '',
            countryCode: ''
        },
        mode: "onChange",
    });


    // 2. Define a submit handler.
    async function onSubmit(values: z.infer<typeof SAddNewBankAccountForm>, e?: React.BaseSyntheticEvent) {
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
            <PageHeader title={"Add new bank account"} subtitle={" Donec gravida tellus at sapien aliquet convallis. Ut euismod tortor eu turpis consectetur placerat. Praesent scelerisque vestibulum nibh, eget rutrum odio condimentum eget. "}/>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8 mx-auto bg-white shadow rounded-lg p-6 max-w-[800px] flex flex-col justify-center ">
                    <div className="space-y-2">
                        <FormField
                            control={form.control}
                            name="iban"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>IBAN</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide your IBAN" {...field} onInput={()=>{
                                            const countryFromIBAN = form.watch('iban').slice(0,2);

                                            if(countries.find(
                                                (country) => country.value === countryFromIBAN)) {
                                                form.setValue("countryCode", countryFromIBAN )
                                            }
                                        }
                                        } />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="fullname"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Full Name</FormLabel>
                                    <FormControl>
                                        <Input type="text" placeholder="Please provide your full name" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />


                        <FormField
                            control={form.control}
                            name="bankName"
                            render={({field}) => (
                                <FormItem className="text-left w-full">
                                    <FormLabel>Bank Name </FormLabel>
                                    <FormControl>
                                        <Input placeholder="Please provide name of your bank" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="swiftCode"
                            render={({field}) => (
                                <FormItem className="text-left">
                                    <FormLabel>Swift Code</FormLabel>
                                    <FormControl>
                                        <Input type="text" placeholder="Please provide Swift code" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="countryCode"
                            render={({field}) => (
                                <FormItem className="text-left flex flex-col">
                                    <FormLabel>Country</FormLabel>
                                    <Popover>
                                        <PopoverTrigger asChild>
                                            <FormControl>
                                                <Button
                                                    variant="outline"
                                                    role="combobox"
                                                    className={cn(
                                                        "w-full justify-between",
                                                        !field.value && "text-muted-foreground"
                                                    )}
                                                >
                                                    {field.value
                                                        ? countries.find(
                                                            (country) => country.value === field.value
                                                        )?.label
                                                        : "Select country"}
                                                    <ChevronsUpDown className="opacity-50"/>
                                                </Button>
                                            </FormControl>
                                        </PopoverTrigger>
                                        <PopoverContent className="w-[200px] p-0">
                                            <Command>
                                                <CommandInput
                                                    placeholder="Search country..."
                                                    className="h-9"

                                                />
                                                <CommandList>
                                                    <CommandEmpty>No country found.</CommandEmpty>
                                                    <CommandGroup>
                                                        {countries.map((country) => (
                                                            <CommandItem
                                                                value={country.label}
                                                                key={country.value}
                                                                onSelect={() => {
                                                                    form.setValue("countryCode", country.value)
                                                                }}
                                                            >
                                                                {country.label}
                                                                <Check
                                                                    className={cn(
                                                                        "ml-auto",
                                                                        country.value === field.value
                                                                            ? "opacity-100"
                                                                            : "opacity-0"
                                                                    )}
                                                                />
                                                            </CommandItem>
                                                        ))}
                                                    </CommandGroup>
                                                </CommandList>
                                            </Command>
                                        </PopoverContent>
                                    </Popover>
                                </FormItem>
                            )}
                        />
                    </div>
                    <Button
                        type="submit"
                        className={`self-end`}
                    >
                        Add
                    </Button>
                </form>
            </Form>
        </div>)
}

export default AddNewBankAccount;
