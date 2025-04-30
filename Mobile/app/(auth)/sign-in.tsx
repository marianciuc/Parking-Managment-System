import React, {useContext, useState} from 'react';
import {
    Text,
    TouchableOpacity,
    View,
    Image,
    TextInput, Platform, Keyboard,
    KeyboardAvoidingView, TouchableWithoutFeedback
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {ArrowLeft, Eye, EyeOff} from 'lucide-react-native';
import images from '../../constants/images';
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import {Context} from "@/app/_layout";
import {Link} from 'expo-router/build/link/Link';
import InputHandy from "@/app/components/input-handy";
import ButtonHandy from "@/app/components/button-handy";

const SignIn = () => {
    const {userStore} = useContext(Context)

    // Email and Password states
    const [email, setEmail] = useState('');
    const router = useExpoRouter();
    const [password, setPassword] = useState('');
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleArrowClick = () => {
        router.navigate("(auth)/welcome")
    };


    const handleLogin = async () => {
        userStore.signInAuthentication({
            password, email
        }).then(async (response) => {
            console.log("User signed in successfully")
            if (userStore.isAuthenticated)
                router.replace("/(root)/explore")
        })
    };

    const handleGoogleLogin = async () => {
        /*setLoading(true);

        const response = await getGoogleSignInURL();
        const authURL = response.data;

        if (!authURL) {
            console.log("Auth URL is missing");
        }

        const result = await WebBrowser.(authURL);
        console.log("URL result: ", result);
        setLoading(false);*/


    }

    return (
        <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
                <SafeAreaView className="h-full w-full bg-white">
                    <View className="flex-col h-full mx-5 justify-between my-5 bg-white">
                        {/* Header Section */}

                        <View className={""}>
                            <View className="flex-row w-full items-center justify-between">
                                <TouchableOpacity onPress={handleArrowClick}>
                                    <ArrowLeft className={"size-24"} color="#000000"/>
                                </TouchableOpacity>
                                <Image source={images.logoBlue} className="w-7 h-8 relative overflow-hidden"/>
                            </View>
                            {/* Title Section */}
                            <View className="flex-col mt-8">
                                <Text className="text-black text-3xl font-poppins-semibold leading-snug">Log in</Text>
                                <Text className="text-black text-m font-poppins leading-snug">
                                    Please fill your details
                                </Text>
                            </View>

                            {/* Email Input Section */}
                            <View className="flex-col mt-10">
                                {/*<Text className="text-black text-sm font-normal font-['Roboto'] leading-[21px]">Email
                                address</Text>
                            <TextInput
                                className="w-full h-14 border border-gray-300 rounded-md p-5 text-black mt-1.5"
                                placeholder="Your email"
                                placeholderTextColor="#7b7b7b"
                                value={email}
                                onChangeText={setEmail}
                            />*/}
                                <InputHandy value={email} label={"Enter your email"} type={"email"} onChange={setEmail}
                                            placeholder={"Your email"}/>
                            </View>

                            {/* Password Input Section */}
                            <View className="flex-col mt-3">
                                {/*<Text
                                className="text-black text-sm font-normal font-['Roboto'] leading-[21px]">Password</Text>
                            <View
                                className="w-full h-14 border border-gray-300 rounded-md flex-row items-center mt-1.5">
                                 Password Input
                                <TextInput
                                    className="flex-1 h-full px-4 text-black font-poppins"
                                    placeholder="Password"
                                    placeholderTextColor="#7b7b7b"
                                    value={password}
                                    onChangeText={setPassword}
                                    secureTextEntry={!isPasswordVisible} // Toggles password visibility
                                />


                                 Eye Icon
                                <TouchableOpacity
                                    onPress={() => setIsPasswordVisible((prev) => !prev)}
                                    className="pr-4"
                                    activeOpacity={0.7}
                                >
                                    {isPasswordVisible ? (
                                        <EyeOff size={20} color="#000000"/>
                                    ) : (
                                        <Eye size={20} color="#000000"/>
                                    )}
                                </TouchableOpacity>
                            </View>*/}
                                <InputHandy value={password} label={"Password"} type={"password"} onChange={setPassword}
                                            placeholder={"Password"} passwordicon={true}/>

                                {/* Forgot Password Text */}
                                <View className="flex-row justify-end mt-2">

                                    <View>
                                        <Link href={"/(auth)/forgot-password"}>
                                            <Text className="text-blue-500 text-base font-normal font-['Roboto']">
                                                Forgot password?
                                            </Text>
                                        </Link>
                                    </View>

                                </View>
                            </View>
                        </View>


                        <View className={"justify-end mb-4"}>
                            <ButtonHandy onPress={handleLogin} label={"Continue"} style={"primary"}/>


                            <TouchableOpacity
                                onPress={handleGoogleLogin}
                                className={"bg-white border border-gray-300 rounded-3xl w-full py-4 mt-5 h-14"}
                            >
                                <View className="flex-row items-center justify-center">
                                    <Image source={images.google} className="w-5 h-5" resizeMode="contain"/>
                                    <Text className="text-black ml-2 font-poppins-semibold">Continue with Google</Text>
                                </View>
                            </TouchableOpacity>

                            <TouchableOpacity
                                onPress={handleLogin}
                                className={"bg-white border border-gray-300 rounded-3xl w-full py-4 mt-5 h-14"}
                            >
                                <View className="flex-row items-center justify-center">
                                    <Image source={images.apple} className="w-5 h-5" resizeMode="contain"/>
                                    <Text className="text-black ml-2 font-poppins-semibold">Continue with Apple</Text>
                                </View>
                            </TouchableOpacity>
                        </View>

                    </View>
                </SafeAreaView>
            </TouchableWithoutFeedback>
        </KeyboardAvoidingView>
    );
};

export default SignIn;
