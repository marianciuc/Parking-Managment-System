import {TouchableOpacity, View} from "react-native";
import {Car, Map, ListCheck, Triangle, User} from "lucide-react-native";
import React from "react";
import {useRouter} from "expo-router";

const BottomMenu = () => {
    const router = useRouter();

    return (
        <View className="absolute bottom-0 left-0 right-0 h-[60px] mx-4 mb-6 rounded-[22px] bg-white shadow-md justify-center">
            <View className="flex-row justify-around py-4">
                <TouchableOpacity className="items-center" onPress={() => router.navigate('/map-search')}>
                    <Map size={28} color="#438eff" />
                </TouchableOpacity>
                <TouchableOpacity className="items-center" onPress={() => router.navigate('/parking-sessions')}>
                    <ListCheck size={28} color="#438eff" />
                </TouchableOpacity>
                <TouchableOpacity className="items-center" onPress={() => router.navigate('/explore')}>
                    <Triangle fill="#438eff" size={28} color="#438eff" />
                </TouchableOpacity>
                <TouchableOpacity className="items-center" onPress={() => router.navigate('/owned-cars')}>
                    <Car size={28} color="#438eff" />
                </TouchableOpacity>
                <TouchableOpacity className="items-center" onPress={() => router.navigate('/profile')}>
                    <User size={28} color="#438eff" />
                </TouchableOpacity>
            </View>
        </View>
    );
};

export default BottomMenu;