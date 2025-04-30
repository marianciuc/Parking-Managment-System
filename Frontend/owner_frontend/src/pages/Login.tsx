
import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs"
import LoginForm from "@/components/forms/LoginForm.tsx";
import RegistrationForm from "@/components/forms/RegistrationForm.tsx";


function Login() {
return  (
    <div className="flex flex-col items-center justify-center w-full h-full max-w-screen gap-20 bg-white p-6 rounded-lg shadow">
        <img src="/logo_text_blue.svg" alt="Logo"/>
        <Tabs defaultValue="signIn" className="w-[400px] gap-5">
            <div className="image-background flex flex-col p-2 items-center justify-center shadow rounded-lg mb-6 ">
                <TabsList className="">
                    <TabsTrigger value="signIn" className="dark:bg-transparent">Sign In</TabsTrigger>
                    <TabsTrigger value="signUp">Sign Up</TabsTrigger>
                </TabsList>
            </div>
        <TabsContent value="signIn" className="mt-5">
            <LoginForm></LoginForm>
        </TabsContent>
        <TabsContent value="signUp" className="mt-5">
            <RegistrationForm></RegistrationForm>
        </TabsContent>
    </Tabs>
    </div>
)
}

export default Login