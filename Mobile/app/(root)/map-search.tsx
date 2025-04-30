import React from 'react';
import { SafeAreaView, View, TextInput, TouchableOpacity } from 'react-native';
import { ArrowLeft, Search } from "lucide-react-native";
import Map from "../components/Map"; // Компонент карты с ParkingMarkers внутри
import SelectedParking from "@/app/components/SelectedParking";
import ParkingProvider from '@/app/providers/ParkingProvider';

const MapSearch = () => {


    return (
        <ParkingProvider>
            <View className="flex-1">
                {/* Компонент карты, который содержит маркеры */}
                <View className="flex-1">
                    <Map />
                </View>

                {/* Компонент BottomSheet с выбранной парковкой */}
                <SelectedParking />

                {/* Поисковая панель с SafeAreaView */}
                <View className="absolute top-0 left-0 right-0 z-10">
                    <SafeAreaView>
                        <View className="flex-row items-center bg-white rounded-[20px] mx-2.5 mt-2.5 px-[15px] py-2.5">
                            {/* Кнопка "назад" */}
                            <TouchableOpacity>
                                <ArrowLeft size={26} color="#757575" />
                            </TouchableOpacity>

                            {/* Контейнер для поискового поля и кнопки */}
                            <View className="flex-1 flex-row items-center ml-2.5">
                                {/* Поле для ввода поиска */}
                                <TextInput
                                    className="flex-1 py-2 px-3 text-base"
                                    placeholder="Find location"
                                    placeholderTextColor="#828282"
                                />

                                {/* Кнопка поиска */}
                                <TouchableOpacity className="ml-2.5">
                                    <Search size={26} color="#757575" />
                                </TouchableOpacity>
                            </View>
                        </View>
                    </SafeAreaView>
                </View>
            </View>
        </ParkingProvider>
    );
};

export default MapSearch;