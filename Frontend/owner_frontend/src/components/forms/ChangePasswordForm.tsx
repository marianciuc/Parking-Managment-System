
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
    oldPassword: "",
    newPassword: "",
    confirmNewPassword: "",
};

// const BUTTON_CLASSES = {
//     valid: "bg-neon hover:bg-neon-dark",
//     invalid: "bg-jordy hover:bg-gray-400",
// };

const ERROR_MESSAGES = {
    email: "Email must be at least 2 characters.",
    invalidEmail: "Invalid email address.",
    password: "Password must be at least 8 characters.",
    confirmPassword: "Passwords must match!",
};

// Zod schema for form validation
const formSchema = z
    .object({
        oldPassword: z.string().min(8, { message: ERROR_MESSAGES.password }),
        newPassword: z.string().min(8, { message: ERROR_MESSAGES.password }),
        confirmNewPassword: z
            .string()
            .min(8, { message: ERROR_MESSAGES.password }),
    })
    .refine((data) => data.newPassword === data.confirmNewPassword, {
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
export function ChangePasswordForm() {
    const formMethods = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: DEFAULT_FORM_VALUES,
        mode: "onChange",
        criteriaMode: "all", // Ensures all validation errors are detailed
    });

    const { userStore } = useContext(Context);


    const handleFormSubmit = async (values: z.infer<typeof formSchema>) => {
        try {
            await userStore.changPassword(values.oldPassword,
               values.newPassword,
            );
            // console.log("Registration successful:", values);
            // window.location.reload();
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
                            name="oldPassword"
                            label="Old Password"
                            type="password"
                            placeholder="Provide your old password"
                            control={formMethods.control}
                        />
                        <CustomFormInput
                            name="newPassword"
                            label="New Password"
                            placeholder="Enter a new secure password"
                            type="password"
                            control={formMethods.control}
                        />
                        <CustomFormInput
                            name="confirmNewPassword"
                            label="Confirm New Password"
                            placeholder="Repeat your new password"
                            type="password"
                            control={formMethods.control}
                        />
                    </div>
                    <div className="flex flex-row justify-end w-full">
                    <Button
                        type="submit"
                        className={``}
                        disabled={!isFormValid}
                    >
                        Save
                    </Button>
                    </div>
                </form>
            </Form>
        </div>
    );
}

export default ChangePasswordForm;