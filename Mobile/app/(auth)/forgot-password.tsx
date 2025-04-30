import {
    View,
    Text,
    TouchableOpacity,
    Image,
    TextInput,
    KeyboardAvoidingView,
    Platform,
    TouchableWithoutFeedback,
    Keyboard,
} from 'react-native'
import React, {useState} from 'react'
import {SafeAreaView} from "react-native-safe-area-context";
import {ArrowLeft} from "lucide-react-native"
import images from "@/constants/images";
import {useNavigation} from "@react-navigation/native";
import ButtonHandy from "@/app/components/button-handy";
import InputField from "@/app/components/input-handy";
import {useExpoRouter} from "expo-router/build/global-state/router-store";



const ForgotPassword = () => {

    const router = useExpoRouter();
    const navigation = useNavigation();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);

    const handleArrowClick = () => {
        navigation.goBack();
    };

    const handleOtpVerification = () => {
        router.navigate("/otp-verification")
    }

    return (

        <KeyboardAvoidingView  behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>
            <TouchableWithoutFeedback onPress={Keyboard.dismiss}>
            <SafeAreaView className="h-full w-full bg-white">
                <View className="h-full flex-col justify-between">
                    <View className="flex-col mx-5 inline-flex my-5 bg-white">
                        <View className="flex-row w-full items-center justify-between">
                            <TouchableOpacity onPress={handleArrowClick}>
                                <ArrowLeft className={"size-24"} color="#000000" />
                            </TouchableOpacity>
                            <Image source={images.logoBlue} className="w-7 h-8 relative overflow-hidden" />
                        </View>
                        <View className="flex-col mt-8 mb-8">
                            <Text className="text-[#333333] text-3xl font-poppins-semibold leading-snug mb-2">Password recovery</Text>
                            <Text className="text-[#333333] text-m font-normal font-poppins leading-snug mt-2">
                                Enter your password and we sent a recovery code{'\n'}to your email!
                            </Text>
                        </View>


                        <InputField
                            label="Enter your email"
                            placeholder="bob@example.com"
                            value={email}
                            onChange={(text: string) => setEmail(text)}
                            type="email"
                        />



                    </View>
                    <View className={"px-4"}>
                        <ButtonHandy
                            label="Continue"
                            onPress={handleOtpVerification}
                            style="primary"
                        />
                    </View>
                </View>

            </SafeAreaView>
                </TouchableWithoutFeedback>
        </KeyboardAvoidingView>

    )
}
export default ForgotPassword
