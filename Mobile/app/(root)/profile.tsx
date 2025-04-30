import {View, Text, SafeAreaView, TouchableOpacity, Image, ScrollView} from 'react-native'
import React, {useContext} from 'react'
import {Context} from "@/app/_layout";
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import Header from "@/app/components/header";
import images from "@/constants/images";
import {ChevronRight, Users, MapPin, Settings, Bell} from "lucide-react-native";

const Profile = () => {
    const router = useExpoRouter();
    const {userStore} = useContext(Context);

    const handleLogout = async () => {
        userStore.logout().then(async (response) => {
            console.log("User logged out")
            router.navigate("(auth)/welcome");
        })
    };

    const handleGeo = () => {
        router.navigate("(root)/additional-registration-info");
    }
    const handleFS = () => {
        router.navigate("(root)/family-share");
    }
    const handleEdit = () => {
        router.navigate("(root)/edit-profile");
    }

    const handleSettings = () => {
        router.navigate("(root)/app-settings");
    }
    const handleOtp = () => {
        router.navigate("/otp-verification");
    }

    return (
        <ScrollView className="flex-1 bg-white">
            <SafeAreaView>
                <Header header="My profile" style="user"/>

                {/* Профильная секция */}
                <View className="items-center pt-6 pb-6 bg-white mb-2">
                    <Image source={images.avatar} className="w-24 h-24 rounded-full"/>
                    <Text className="mt-3 text-gray-900 text-xl font-semibold">
                        Vladimir Marianciuc
                    </Text>
                    <Text className="text-gray-500 text-sm">
                        Active from january 2025
                    </Text>
                </View>

                {/* Информационная секция */}
                <View className="mb-2">
                    <View className="px-5 py-3 bg-white flex-row justify-between items-center border-t border-gray-100">
                        <Text className="text-gray-900 font-semibold">Personal information</Text>
                        <TouchableOpacity onPress={handleEdit}>
                            <Text className="text-[#3f8efc] text-sm font-medium">Edit</Text>
                        </TouchableOpacity>
                    </View>

                    <View className="bg-white">
                        <View className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100">
                            <Text className="text-gray-500">Email</Text>
                            <Text className="text-gray-900">vladimir.marianciuc@gmail.com</Text>
                        </View>

                        <View className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100">
                            <Text className="text-gray-500">Phone number</Text>
                            <Text className="text-gray-900">+48516023312</Text>
                        </View>

                        <View className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100">
                            <Text className="text-gray-500">Location</Text>
                            <Text className="text-gray-900">Poland</Text>
                        </View>
                    </View>
                </View>

                {/* Разделы */}
                <View className="mb-2">
                    <View className="px-5 py-3 bg-white border-t border-gray-100">
                        <Text className="text-gray-900 font-semibold">Sections</Text>
                    </View>

                    <View className="bg-white">
                        <TouchableOpacity
                            className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100"
                            onPress={() => router.push('/notifications')}
                        >
                            <View className="flex-row items-center">
                                <View className="w-8 h-8 rounded-full bg-blue-50 items-center justify-center mr-3">
                                    <Bell size={16} color="#3f8efc"/>
                                </View>
                                <Text className="text-gray-900">Notifications</Text>
                            </View>
                            <ChevronRight size={18} color="#D1D5DB" />
                        </TouchableOpacity>

                        <TouchableOpacity
                            className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100"
                            onPress={handleSettings}
                        >
                            <View className="flex-row items-center">
                                <View className="w-8 h-8 rounded-full bg-blue-50 items-center justify-center mr-3">
                                    <Settings size={16} color="#3f8efc"/>
                                </View>
                                <Text className="text-gray-900">Settings</Text>
                            </View>
                            <ChevronRight size={18} color="#D1D5DB" />
                        </TouchableOpacity>

                        <TouchableOpacity
                            className="py-3.5 px-5 flex-row items-center justify-between border-t border-gray-100"
                            onPress={handleFS}
                        >
                            <View className="flex-row items-center">
                                <View className="w-8 h-8 rounded-full bg-blue-50 items-center justify-center mr-3">
                                    <Users size={16} color="#3f8efc"/>
                                </View>
                                <Text className="text-gray-900">Family Share</Text>
                            </View>
                            <ChevronRight size={18} color="#D1D5DB" />
                        </TouchableOpacity>
                    </View>
                </View>

                {/* Выход */}
                <TouchableOpacity
                    className="mx-5 mt-6 mb-10 py-3.5 bg-white rounded-xl items-center border border-red-100"
                    onPress={handleLogout}
                >
                    <Text className="text-red-500 font-medium">Logout</Text>
                </TouchableOpacity>
            </SafeAreaView>
        </ScrollView>
    )
}

export default Profile