"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useContext } from "react";
import { Context } from "@/main.tsx";

// Constants for default values and form settings
const DEFAULT_FORM_VALUES = {
    email: "",
    password: "",
    confirmPassword: "",
};

const BUTTON_CLASSES = {
    valid: "bg-neon hover:bg-neon-dark",
    invalid: "bg-jordy hover:bg-gray-400",
};

const ERROR_MESSAGES = {
    email: "Email must be at least 2 characters.",
    invalidEmail: "Invalid email address.",
    password: "Password must be at least 8 characters.",
    confirmPassword: "Passwords must match!",
};

// Zod schema for form validation
const formSchema = z
    .object({
        email: z
            .string()
            .min(2, { message: ERROR_MESSAGES.email })
            .email(ERROR_MESSAGES.invalidEmail),
        password: z.string().min(8, { message: ERROR_MESSAGES.password }),
        confirmPassword: z
            .string()
            .min(8, { message: ERROR_MESSAGES.password }),
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: ERROR_MESSAGES.confirmPassword,
        path: ["confirmPassword"], // Target field for the error
    });

// Reusable form input component
const CustomFormInput = ({ name, label, placeholder, type = "text", control }: {
    name: string;
    label: string;
    placeholder: string;
    type?: string;
    control: any;
}) => {
    return (
        <FormField
            control={control}
            name={name}
            render={({ field }) => (
                <FormItem className="text-left">
                    <FormLabel>{label}</FormLabel>
                    <FormControl>
                        <Input type={type} placeholder={placeholder} {...field} />
                    </FormControl>
                    <FormMessage />
                </FormItem>
            )}
        />
    );
};

// Main RegistrationForm component
export function RegistrationForm() {
    const formMethods = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: DEFAULT_FORM_VALUES,
        mode: "onChange",
        criteriaMode: "all", // Ensures all validation errors are detailed
    });

    const { userStore } = useContext(Context);


    const handleFormSubmit = async (values: z.infer<typeof formSchema>) => {
        try {
            await userStore.registration({
                email: values.email,
                password: values.password,
            });
            console.log("Registration successful:", values);
            window.location.reload();
        } catch (error) {
            console.error("Registration failed", error);
        }
    };

    // Dynamically track validity from form state
    const isFormValid = formMethods.formState.isValid;

    return (
        <div className="w-full min-w-fit justify-center gap-0 items-center mx-auto">
            <Form {...formMethods}>
                <form
                    onSubmit={formMethods.handleSubmit(handleFormSubmit)}
                    className="space-y-8"
                >
                    <div className="space-y-2">
                        <CustomFormInput
                            name="email"
                            label="Email"
                            placeholder="Enter your email"
                            control={formMethods.control}
                        />
                        <CustomFormInput
                            name="password"
                            label="Password"
                            placeholder="Enter a secure password"
                            type="password"
                            control={formMethods.control}
                        />
                        <CustomFormInput
                            name="confirmPassword"
                            label="Confirm Password"
                            placeholder="Repeat your password"
                            type="password"
                            control={formMethods.control}
                        />
                    </div>
                    <Button
                        type="submit"
                        className={`w-full ${
                            isFormValid ? BUTTON_CLASSES.valid : BUTTON_CLASSES.invalid
                        }`}
                        disabled={!isFormValid}
                    >
                        Sign up
                    </Button>
                </form>
            </Form>
        </div>
    );
}

export default RegistrationForm;