import React, { useEffect, useRef, useState } from "react";
import { useParking } from "@/app/providers/ParkingProvider";
import BottomSheet, { BottomSheetView } from "@gorhom/bottom-sheet";
import {
    View,
    Text,
    TouchableOpacity,
    Linking,
    Platform,
    ScrollView
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { LinearGradient } from "expo-linear-gradient";


interface IOpeningHours {
    dayOfWeek: DayOfWeek;
    openingTime?: [number, number];
    closingTime?: [number, number];
    isClosed?: boolean;
}
interface ICapacity {
    capacity: number;
    occupiedSpaces: number;
    placesForDisabled: number;
    occupiedPlacesForDisabled: number;
    placesForElectricCars: number;
    occupiedPlacesForElectricCar: number;
}

interface IAddress {
    id: string | undefined | null;
    countryCode: string;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
    latitude: number ;
    longitude: number;
    isBelongToAnyInstitution: boolean;
    institutionName: string | undefined | null;
    recordStatus: string | undefined | null;
    creationDate: string | undefined | null;
    modificationDate: string | undefined | null;
}
enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

interface IParkingTag{
    id: string | undefined | null;
    name: string | undefined | null;
    description: string | undefined | null;
}

interface IParking {
    id: string;
    name: string;
    imageUrl: string;
    address: IAddress;
    capacity:number;
    occupiedSlots:number;
    is24h : boolean;
    tags : IParkingTag[];
    workingTimeList?: IOpeningHours[];
}


export enum ParkingStatus {
    CREATION_PROCESS = "CREATION_PROCESS",
    TEMPORARY_CLOSED = "TEMPORARY_CLOSED",
    MODERATE = "ON_MODERATION",
    FULL = "FULL",
    CLOSED = "CLOSED",
    OPEN = "OPEN"
}

const parkingTags = [
    { name: "🚗 Covered", description: "🏡 Keep your car dry and cozy under a roof!" },
    { name: "☀️ Open Air", description: "🌳 Enjoy the fresh air while parking under the sky." },
    { name: "⚡ Electric Charging", description: "🔋 Charge up your EV with on-site charging stations." },
    { name: "♿ Accessible", description: "✨ Convenient spaces for everyone’s needs." },
    { name: "🎩 Valet", description: "🚘 Let someone else do the parking for you in style." },
    { name: "🔒 Secure", description: "🛡️ Safety first! Monitored and secure parking area." },
    { name: "🚗 Compact", description: "➡️ Perfectly snug for smaller vehicles." },
    { name: "👨‍👩‍👧‍👦 Family", description: "🚘 Extra room for families with kids in tow." },
    { name: "🏍️ Motorcycle", description: "📍 Dedicated spots for two-wheelers." },
    { name: "🚴‍♂️ Bicycle", description: "🔐 Secure racks for your bikes." },
    { name: "⛅ Underground", description: "🚇 Park underground and beat the weather." },
    { name: "🕒 Long Term", description: "📆 Perfect for extended stays." },
    { name: "⏱️ Short Term", description: "🛍️ Quick and easy parking for short visits." },
    { name: "💎 Premium", description: "🚗 Exclusive spots for a VIP parking experience." },
    { name: "🌱 Eco-Friendly", description: "♻️ Green parking solutions for a sustainable future." },
    { name: "✈️ Airport", description: "🚘 Hassle-free parking for jet-setters." },
    { name: "🏨 Hotel", description: "🛎️ Convenient parking right at your stay." },
    { name: "🛍️ Mall", description: "🚗 Easy access to shopping and fun." },
    { name: "🏟️ Stadium", description: "🎉 Ready for the big game? Park here!" },
    { name: "🚉 Nearby Transit", description: "🚌 Park and hop onto public transport." }
];
export default function SelectedParking() {
    const { selectedParking } = useParking();
    const bottomSheetRef = useRef<BottomSheet>(null);
    const [rating, setRating] = useState(4.5); // Пример рейтинга
    const [reviewCount, setReviewCount] = useState(42); // Пример количества отзывов

    // Примеры тегов для парковки
    const [tags, setTags] = useState(selectedParking.tags);

    useEffect(() => {
        if (selectedParking) {
            bottomSheetRef.current?.expand();
        } else {
            bottomSheetRef.current?.close();
        }
    }, [selectedParking]);

    // Функция для открытия маршрута в картах
    const openMapsWithDirections = () => {
        if (!selectedParking) return;

        const { ad, long, name } = selectedParking;
        const destination = `${selectedParking.address.latitude},${selectedParking.address.longitude}`;
        const label = encodeURIComponent(name);

        // Для iOS откроем Apple Maps, для Android - Google Maps
        if (Platform.OS === 'ios') {
            Linking.openURL(`http://maps.apple.com/?daddr=${destination}&dirflg=d&t=m`);
        } else {
            Linking.openURL(`https://www.google.com/maps/dir/?api=1&destination=${destination}&destination_place_id=${label}`);
        }
    };

    // Функция для открытия экрана отзывов
    const openReviews = () => {
        // Здесь можно добавить навигацию к экрану отзывов
        // Например: navigation.navigate('Reviews', { parkingId: selectedParking.id });
        console.log("Открываем отзывы для парковки", selectedParking?.id);
    };


    // Функция для отображения звезд рейтинга
    const renderStars = (rating: number) => {
        const stars = [];
        const fullStars = Math.floor(selectedParking?.rating || 4);
        const hasHalfStar = rating % 1 !== 0;

        for (let i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars.push(<Ionicons key={i} name="star" size={16} color="#FFD700" />);
            } else if (i === fullStars && hasHalfStar) {
                stars.push(<Ionicons key={i} name="star-half" size={16} color="#FFD700" />);
            } else {
                stars.push(<Ionicons key={i} name="star-outline" size={16} color="#FFD700" />);
            }
        }

        return stars;
    };

    if (!selectedParking) {
        return (
            <BottomSheet
                ref={bottomSheetRef}
                index={-1}
                snapPoints={[100, "50%"]}
                handleIndicatorStyle={{ backgroundColor: '#CBD5E0', width: 40 }}
            >
                <BottomSheetView>
                    <Text className="p-4 text-center text-gray-600 text-base">Select parking on the map</Text>
                </BottomSheetView>
            </BottomSheet>
        );
    }

    return (
        <BottomSheet
            ref={bottomSheetRef}
            index={-1}
            snapPoints={[100, "50%"]}
            handleIndicatorStyle={{ backgroundColor: '#CBD5E0', width: 40 }}
        >
            <ScrollView className="flex-1">
                <View className="p-4">
                    {/* Заголовок */}
                    <View className="flex-row justify-between items-start mb-4">
                        <View>
                            <Text className="text-2xl font-bold text-gray-800 mb-1">{selectedParking.name}</Text>
                            <View className="flex-row items-center">
                                <View className="flex-row mr-2">
                                    {renderStars(rating)}
                                </View>
                                <TouchableOpacity onPress={openReviews} className="ml-1">
                                    <Text className="text-gray-600 text-sm underline">{reviewCount} reviews</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                        <View className="bg-blue-50 px-3 py-1.5 rounded-2xl">
                            <Text className="text-blue-500 font-semibold text-xs">
                                {getCategoryName(selectedParking.category)}
                            </Text>
                        </View>
                    </View>

                    {/* Информация */}
                    <View className="bg-gray-100 rounded-xl p-4 mb-5">
                        <InfoItem
                            icon="car-outline"
                            title="Free slots"
                            value={selectedParking.capacity - selectedParking.occupiedSlots}
                        />
                        <InfoItem
                            icon="cash-outline"
                            title="Price"
                            value={12}
                        />
                    </View>

                    <View className="flex-row items-center justify-between mb-6">
                        {/*<TouchableOpacity*/}
                        {/*    className="flex-row items-center justify-center bg-gray-100 rounded-lg py-3 px-3"*/}
                        {/*    onPress={openReviews}*/}
                        {/*>*/}
                        {/*    <Ionicons name="star-outline" size={18} color="#333" />*/}
                        {/*    <Text className="ml-1.5 font-semibold text-gray-800 text-sm">Отзывы</Text>*/}
                        {/*</TouchableOpacity>*/}

                        <TouchableOpacity
                            className="flex-row items-center justify-center bg-blue-400 rounded-lg py-3 px-3 w-full"
                            onPress={openMapsWithDirections}
                        >
                            <Ionicons name="navigate-outline" size={18} color="#fff" />
                            <Text className="ml-1.5 font-semibold text-white text-sm">Route</Text>
                        </TouchableOpacity>
                    </View>


                    {/* Дополнительная информация */}
                    <View className="mb-4">
                        <Text className="text-lg font-bold text-gray-800 mb-2">Parking Spot Information</Text>
                        <Text className="text-sm leading-5 text-gray-600">
                            The parking lot is located near the main attractions of the city.
                            Here you can leave your car under reliable security and
                            take advantage of additional services.
                        </Text>
                    </View>

                    {/* Теги */}
                    <View className="mb-5">
                        <Text className="text-lg font-bold text-gray-800 mb-3">Tags</Text>
                        <View className="flex-row flex-wrap">
                            {tags.map((tag, index) => (
                                <View key={index} className="bg-blue-50 rounded-full px-3 py-1.5 mr-2 mb-2">
                                    <Text className="text-blue-600 text-xs">{tag.name}</Text>
                                </View>
                            ))}
                        </View>
                    </View>

                    {/* Кнопка "Узнать больше" */}
                    <TouchableOpacity
                        className="bg-gray-100 rounded-lg py-3 items-center mb-4"
                        onPress={() => console.log("Подробнее о парковке", selectedParking?.id)}
                    >
                        <Text className="font-semibold text-gray-700">Read more about parking</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>
        </BottomSheet>
    );
}

// Вспомогательный компонент для пунктов информации
function InfoItem({ icon, title, value }) {
    return (
        <View className="flex-row items-center mb-3">
            <Ionicons name={icon} size={24} color="#4A90E2" />
            <View className="ml-3">
                <Text className="text-sm text-gray-500">{title}</Text>
                <Text className="text-base font-bold text-gray-800">{value}</Text>
            </View>
        </View>
    );
}

// Функция для перевода категорий на русский
function getCategoryName(category) {
    switch(category) {
        case 'public': return 'Public';
        case 'private': return 'Private';
        case 'commercial': return 'Commercial';
        case 'free': return 'Free';
        case 'event': return 'Event';
        case 'transport': return 'Transport';
        default: return 'Gove';
    }
}