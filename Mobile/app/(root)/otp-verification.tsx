import {View, Text, SafeAreaView, TouchableWithoutFeedback, Keyboard, TouchableOpacity} from 'react-native'
import React, {useState, useEffect} from 'react'
import Header from "@/app/components/header";
import {OtpInput} from "react-native-otp-entry";
import {fetchVerification, sendVerificationCode} from "@/app/services/VerificationService";
import ButtonHandy from "@/app/components/button-handy";

const OtpVerification = () => {
    const [timeLeft, setTimeLeft] = useState(60);
    const [canResend, setCanResend] = useState(false);
    const [otp, setOtp] = useState("");

    useEffect(() => {
        if (timeLeft > 0) {
            const timer = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
            return () => clearTimeout(timer);
        } else {
            setCanResend(true);
        }
    }, [timeLeft]);

    const handleResendCode = () => {
        setTimeLeft(60);
        setCanResend(false);
        sendVerificationCode();
        console.log("Resend code");
    };

    const handleBut = () => {
        sendVerificationCode();
    }

    const handleOtpChange = async (code: string) => {
        setOtp(code);
        if (code.length === 5) {
            await fetchVerification(code);
            console.log("code: ", code);
        }
    };

    return (
        <TouchableWithoutFeedback onPress={() => Keyboard.dismiss()}>
            <SafeAreaView>
                <Header header={""} style={"auth"}/>
                <View className="px-4 w-full">
                    <Text className="text-4xl font-semibold leading-tight mb-4">
                        Enter code
                    </Text>
                    <Text className="text-black/70 text-base font-poppins leading-tight w-10/12">
                        We’ve sent an SMS with an activation code to your email
                    </Text>
                    <View className="mt-20 justify-center items-center">
                        <View className={"p-8 flex-1 justify-center mb-24"}>
                            <OtpInput numberOfDigits={5} focusColor={"#438eff"} onTextChange={handleOtpChange}/>
                        </View>
                        {canResend ? (
                            <TouchableOpacity onPress={handleResendCode}>
                                <Text className="text-blue-500 text-base font-poppins leading-tight">Resend Code</Text>
                            </TouchableOpacity>
                        ) : (
                            <Text className="text-gray-500 text-base font-poppins leading-tight">Send code
                                again {timeLeft}s</Text>
                        )}
                    </View>
                </View>
            </SafeAreaView>
        </TouchableWithoutFeedback>
    )
}
export default OtpVerification
