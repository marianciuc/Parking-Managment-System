import {useNavigation} from "@react-navigation/native";
import * as Location from "expo-location";
import {Alert, Image, SafeAreaView, Text, TouchableOpacity, View} from "react-native";
import {ArrowLeft, MapPin} from "lucide-react-native";
import images from "@/constants/images";
import React from "react";
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import ButtonHandy from "@/app/components/button-handy";

const GeoPermission = () => {

    const router = useExpoRouter();
    const navigation = useNavigation();

    const handleNotificationsPermission = () => {
        router.navigate("/notifications-permission")
    }

    const handleArrowClick = () => {
        navigation.goBack();
    };

    const requestPermissions = async () => {
        const {status} = await Location.requestForegroundPermissionsAsync();
        if (status !== 'granted') {
            Alert.alert(
                'Geolocation Permission Denied',
                'If you want to change this, go to Settings -> HandyParking -> Permissions'
            );
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
                <Text className="text-3xl font-poppins-semibold text-left">Enable Location Services!</Text>
            </View>
            <View className="flex-1 items-center mt-10">
                <MapPin size={200} color={"#438EFF"}/>
                <View className={"w-80 mt-10"}>
                    <Text className={"text-center font-poppins text-base"}>Turn on location services to find nearby
                        parking spots effortlessly. Get accurate suggestions and real-time availability based on your
                        current location.</Text>
                </View>
            </View>

            <View className={"px-4"}>
                <ButtonHandy
                    label="Enable location services"
                    onPress={requestPermissions}
                    style="primary"
                />

                <ButtonHandy
                    label="Skip for now"
                    onPress={handleNotificationsPermission}
                    style="secondary"
                />
            </View>

        </SafeAreaView>


    )
}
export default GeoPermission;