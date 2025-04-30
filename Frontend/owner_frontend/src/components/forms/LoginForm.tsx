import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useContext, useState } from "react";
import { Context } from "@/main.tsx";
import {useToast} from "@/hooks/use-toast.ts";

const formSchema = z.object({
    email: z.string().email("Invalid email address."),
    password: z.string().min(8, { message: "Password must be at least 8 characters." }),
});

const LOGIN_ERROR_MESSAGE = "Login failed. Please check your credentials.";

const useFormSetup = () => {
    return useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
            password: "",
        },
        mode: "onChange",
    });
};

export function LoginForm() {
    const { userStore: authStore } = useContext(Context);
    const [serverError, setServerError] = useState<string | null>(null);
    const toast = useToast();
    const form = useFormSetup();

    const onSubmit = async (values: z.infer<typeof formSchema>) => {
        try {
            await authStore.login({ email: values.email, password: values.password });
            console.log("Login successful");
            toast.toast({title: "Login successful", description: "You are now logged in."})
        } catch (error) {
            console.error(LOGIN_ERROR_MESSAGE, error);
            toast.toast({title: "Login failed", description: LOGIN_ERROR_MESSAGE, variant: "destructive"})
            setServerError(LOGIN_ERROR_MESSAGE);
        }
    };

    const renderFormField = (
        name: "email" | "password",
        label: string,
        placeholder: string,
        type: string = "text",
        description?: string
    ) => {
        return (
            <FormField
                control={form.control}
                name={name}
                render={({ field }) => (
                    <FormItem className="text-left">
                        <FormLabel>{label}</FormLabel>
                        <FormControl>
                            <Input type={type} placeholder={placeholder} {...field} />
                        </FormControl>
                        {description && <FormDescription>{description}</FormDescription>}
                        <FormMessage />
                    </FormItem>
                )}
            />
        );
    };

    return (
        <div className="w-full min-w-fit justify-center items-center mx-auto">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                    <div className="space-y-2">
                        {renderFormField("email", "Email", "Enter your email")}
                        {renderFormField(
                            "password",
                            "Password",
                            "Enter your password",
                            "password",
                            "If you have forgotten your password, please contact us by email."
                        )}
                    </div>
                    <Button
                        type="submit"
                        className={`w-full ${form.formState.isValid ? "bg-neon hover:bg-neon-dark" : "bg-jordy hover:bg-gray-400"}`}
                        disabled={!form.formState.isValid}
                    >
                        Sign in
                    </Button>
                    {serverError && <p className="text-red-500">{serverError}</p>}
                </form>
            </Form>
        </div>
    );
}

export default LoginForm;