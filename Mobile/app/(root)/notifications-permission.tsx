import {View, Text, TouchableOpacity, Image, SafeAreaView, Alert} from 'react-native'
import React, {useEffect} from 'react'
import {ArrowLeft, Bell} from "lucide-react-native";
import {useNavigation} from "@react-navigation/native";
import images from "@/constants/images";
import * as Notifications from 'expo-notifications';
import ButtonHandy from "@/app/components/button-handy";
import {useExpoRouter} from "expo-router/build/global-state/router-store";


const NotificationsPermission = () => {

    const router = useExpoRouter();
    const navigation = useNavigation();
    const handleArrowClick = () => {
        navigation.goBack();
    };

    const handleMain = () => {
        router.push("/(root)/explore")
    }

    // Функция запроса разрешений
    const requestPermissions = async () => {
        // Проверяем текущий статус разрешений
        const { status } = await Notifications.getPermissionsAsync();

        // Если статус не 'granted', то снова запрашиваем разрешение
        if (status !== 'granted') {
            const { status: newStatus } = await Notifications.requestPermissionsAsync();
            if (newStatus !== 'granted') {
                Alert.alert(
                    'Notification Permission Denied',
                    'We need notification permission to send you alerts.'
                );
            }
        } else {
            Alert.alert('Notifications are already enabled!');
        }
    };

    return (

        <SafeAreaView className="w-full h-full bg-color-white">
            <View className="flex-row justify-between px-4 mt-8">
                <TouchableOpacity onPress={handleArrowClick}>
                    <ArrowLeft className={"size-24"} color="#000000"/>
                </TouchableOpacity>
                <Image source={images.logoBlue} className="w-7 h-8 relative overflow-hidden"/>
            </View>
            <View className="px-4 mt-10">
                <Text className="text-3xl font-poppins-semibold text-left">Turn on notifications {"\n"}to stay updated!</Text>
            </View>
            <View className="flex-1 items-center mt-10">
                <Bell size={200} color={"#438EFF"}/>
                <View className={"w-80 mt-10"}>
                    <Text className={"text-center font-poppins text-base"}>Enable notifications to never miss an update! Get real-time alerts about parking availability, reminders, and exclusive offers directly to your device.</Text>
                </View>
            </View>

            <View className={"px-4"}>
                <ButtonHandy
                    label="Enable notifications"
                    onPress={requestPermissions}
                    style="primary"
                />

                <ButtonHandy
                    label="Skip for now"
                    onPress={handleMain}
                    style="secondary"
                />
            </View>




        </SafeAreaView>


    )
}
export default NotificationsPermission
