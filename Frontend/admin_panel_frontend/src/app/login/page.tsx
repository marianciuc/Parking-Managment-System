'use client';

import {useState} from 'react';
import {useRouter} from 'next/navigation';

import {Button} from '@/components/ui/button';
import {Input} from '@/components/ui/input';
import {Context} from "@/providers/ClientContextProvider";
import {useContext} from "react";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs";
import {Car} from "lucide-react";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card";
import {Label} from "@/components/ui/label";
import {toast} from "sonner";

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const router = useRouter();
    const {userStore} = useContext(Context);

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();


        const toastMessage = await userStore.login({email: email, password});

        if (toastMessage.messageType == "success") {
            toast.success("Congratulations! You have successfully logged in to the system. ")
            router.push('/dashboard');
        } else {
            toast.error("Error! You have entered incorrect login or password.", {description: toastMessage.message})
        }
    };

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        if (validateForm()) {
            try {
                await userStore.signUp({email, password});
                router.push('/dashboard');
            } catch (err: any) {
                // setError(err.message || 'Registration failed');
            }
        }
    }

    const validateForm = () => {
        return email.length > 0 && password.length > 0;
    }

    return (
        <div className="flex items-center justify-center h-screen">
            <Tabs defaultValue="login" className="w-[400px]">
                <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="login">Login</TabsTrigger>
                    <TabsTrigger value="register">Register</TabsTrigger>
                </TabsList>
                <TabsContent value="login">
                    <Card>
                        <CardHeader>
                            <CardTitle>Login into HandyParking Administrator Dashbord</CardTitle>
                            <CardDescription>
                                Provide your email and password to sign in to your account. If you don't have an
                                account, you can register a new one by clicking the button below.
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="space-y-2">
                            <div className="space-y-1">
                                <Label htmlFor="email">Email</Label>
                                <Input id="email" placeholder="x.peduarte@gmail.com"
                                       onChange={(e) => setEmail(e.target.value)} value={email}/>
                            </div>
                            <div className="space-y-1">
                                <Label htmlFor="password">Username</Label>
                                <Input id="password" type="password" placeholder="YourStrongPassword"
                                       onChange={(e) => setPassword(e.target.value)} value={password}/>
                            </div>
                        </CardContent>
                        <CardFooter>
                            <Button onClick={handleLogin}>Login</Button>
                        </CardFooter>
                    </Card>
                </TabsContent>
                <TabsContent value="register">
                    <Card>
                        <CardHeader>
                            <CardTitle>Register new Administrator of HandyParking</CardTitle>
                            <CardDescription>
                                This function is only available, if in the system is not registered any Administrator.
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="space-y-2">
                            <div className="space-y-1">
                                <Label htmlFor="email">Email</Label>
                                <Input id="email" placeholder="x.peduarte@gmail.com"
                                       onChange={(e) => setEmail(e.target.value)} value={email}/>
                            </div>
                            <div className="space-y-1">
                                <Label htmlFor="password">Username</Label>
                                <Input id="password" type="password" placeholder="YourStrongPassword"
                                       onChange={(e) => setPassword(e.target.value)} value={password}/>
                            </div>
                        </CardContent>
                        <CardFooter>
                            <Button onClick={handleRegister}>Register</Button>
                        </CardFooter>
                    </Card>
                </TabsContent>
            </Tabs>
        </div>
    );
};

export default LoginPage;