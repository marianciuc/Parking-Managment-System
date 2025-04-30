import {View, Text, ScrollView, Image, TouchableOpacity} from 'react-native'
import React from 'react'
import {SafeAreaView} from "react-native-safe-area-context";
import { ArrowLeft } from 'lucide-react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import images from '../../constants/images';
import icons from '../../constants/icons';
import { useRouter } from 'expo-router';
import {createNavigationContainerRef, useNavigation} from "@react-navigation/native";
import ButtonHandy from "@/app/components/button-handy";

const Onboarding = () => {

    const handleLogin =() => {
        router.push('/(auth)/sign-in');
    };
    const handleSignup =() => {
        router.push('/(auth)/sign-up');
    };
    const router = useRouter();


    return (
        <SafeAreaView className={"bg-white h-full "}>
            <View className="w-full h-[420px] flex-col items-center inline-flex mt-[120px]">

                <View className="flex-1 align-middle items-center justify-center">
                    <Image source={images.welcomeCar} className={"align-middle"} resizeMode="contain" style={{ width: 320, height: 210}} />
                </View>


                <View className="flex-1 justify-center items-center mt-8">
                    <Text className="text-black text-center text-3xl font-bold font-poppins-bold mt-6 px-12">
                        Let's start
                    </Text>

                    <Text className="text-blue-500 text-center text-3xl font-bold font-poppins-bold px-12">
                        Your parking journey!
                    </Text>

                    <Text className="text-black text-center text-m font-poppins-light mt-5 px-6">
                        Simplify your parking with fast, secure, and convenient solutions. Log in to get started!
                    </Text>
                </View>

            </View>

            <View className="mt-24 px-4">
                <ButtonHandy label={"Login"} onPress={handleLogin} style={"primary"}/>
                <ButtonHandy label={"Sign up"} onPress={handleSignup} style={"secondary"}/>
            </View>
        </SafeAreaView>
    )
}
export default Onboarding
