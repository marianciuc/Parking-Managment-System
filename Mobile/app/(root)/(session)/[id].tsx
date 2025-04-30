import {
    View,
    Text,
    ScrollView,
    TouchableOpacity,
    Animated,
    Alert,
    Modal,
    FlatList
} from 'react-native'
import React, { useRef, useState, useEffect } from 'react'
import Header from "@/app/components/header";
import { MapPin, Clock, Car, DollarSign, X, CheckCircle } from "lucide-react-native";
import { Feather } from "@expo/vector-icons";
import { LinearGradient } from 'expo-linear-gradient';
import {useLocalSearchParams} from "expo-router";

const ParkingSessionActive = () => {
    const { id } = useLocalSearchParams();

    // Анимированное значение для таймера с меньшей амплитудой
    const animatedValue = useRef(new Animated.Value(1)).current;
    const [timeElapsed, setTimeElapsed] = useState({ hours: 1, minutes: 32 });
    const [currentParkingZone, setCurrentParkingZone] = useState({
        name: "Wi ZUT Parking Space",
        slot: "25F",
        rate: 10
    });
    const [isZoneModalVisible, setIsZoneModalVisible] = useState(false);
    // Добавляем состояние для хранения уже оплаченной суммы
    const [paidAmount, setPaidAmount] = useState(15.00);

    // Доступные зоны парковки
    const parkingZones = [
        { id: '1', name: "Galaxy Centrum parking", slot: "25F", rate: 10 },
        { id: '2', name: "City Mall parking", slot: "12A", rate: 8 },
        { id: '3', name: "Downtown parking", slot: "05C", rate: 12 },
        { id: '4', name: "Stadium parking", slot: "33B", rate: 7 },
        { id: '5', name: "Park Avenue parking", slot: "18D", rate: 15 },
    ];

    // Исправленная анимация пульсации - с меньшим диапазоном и более мягкими переходами
    useEffect(() => {
        const pulse = Animated.loop(
            Animated.sequence([
                Animated.timing(animatedValue, {
                    toValue: 1.02, // Очень мягкая пульсация
                    duration: 2000, // Более медленная и плавная
                    useNativeDriver: true,
                }),
                Animated.timing(animatedValue, {
                    toValue: 1,
                    duration: 2000,
                    useNativeDriver: true,
                }),
            ])
        );

        pulse.start();

        return () => pulse.stop();
    }, []);

    // Имитация увеличения времени пребывания на парковке
    useEffect(() => {
        const timer = setInterval(() => {
            setTimeElapsed(prevTime => {
                let newMinutes = prevTime.minutes + 1;
                let newHours = prevTime.hours;

                if (newMinutes >= 60) {
                    newMinutes = 0;
                    newHours += 1;
                }

                return { hours: newHours, minutes: newMinutes };
            });
        }, 60000); // Обновление каждую минуту

        return () => clearInterval(timer);
    }, []);

    const handleEndSession = () => {
        Alert.alert(
            "Оплатить и завершить",
            "Вы уверены, что хотите оплатить и завершить текущую парковочную сессию?",
            [
                { text: "Отмена", style: "cancel" },
                { text: "Да, оплатить", style: "default", onPress: () => console.log("Сессия оплачена") }
            ]
        );
    };

    const handleReportIssue = () => {
        Alert.alert(
            "Сообщить о проблеме",
            "Выберите тип проблемы:",
            [
                { text: "Отмена", style: "cancel" },
                { text: "Проблема с оплатой", onPress: () => console.log("Проблема с оплатой") },
                { text: "Неверная информация о парковке", onPress: () => console.log("Неверная информация") },
                { text: "Другое", onPress: () => console.log("Другая проблема") }
            ]
        );
    };

    const handleChangeZone = () => {
        setIsZoneModalVisible(true);
    };

    const selectNewZone = (zone) => {
        Alert.alert(
            "Change zone",
            `Do you want to change zone "${zone.name}" (slot ${zone.slot})?`,
            [
                { text: "Cancel", style: "cancel" },
                {
                    text: "Change",
                    onPress: () => {
                        setCurrentParkingZone(zone);
                        setIsZoneModalVisible(false);
                        Alert.alert("Succcesful", `Вы переместились в зону "${zone.name}" (слот ${zone.slot})`);
                    }
                }
            ]
        );
    };

    // Расчет текущей стоимости
    const calculateCurrentCost = () => {
        const totalMinutes = timeElapsed.hours * 60 + timeElapsed.minutes;
        return ((totalMinutes / 60) * currentParkingZone.rate).toFixed(2);
    };

    // Расчет оставшейся неоплаченной суммы
    const calculateRemainingCost = () => {
        const currentCost = parseFloat(calculateCurrentCost());
        return (currentCost - paidAmount).toFixed(2);
    };

    // Обработчик для дополнительной оплаты
    const handleAdditionalPayment = () => {
        const remainingCost = parseFloat(calculateRemainingCost());
        if (remainingCost <= 0) {
            Alert.alert("Information", "Нет необходимости в дополнительной оплате.");
            return;
        }

        Alert.alert(
            "Additional payment",
            `Pay ${remainingCost.toFixed(2)}$ for current session?`,
            [
                { text: "Cancel", style: "cancel" },
                {
                    text: "Pay",
                    onPress: () => {
                        setPaidAmount(parseFloat(calculateCurrentCost()));
                        Alert.alert("Successful ", "Additional payment successful processed.");
                    }
                }
            ]
        );
    };

    const renderZoneItem = ({ item }) => (
        <TouchableOpacity
            className="flex-row items-center p-4 border-b border-gray-100"
            onPress={() => selectNewZone(item)}
        >
            <View className="w-10 h-10 bg-blue-100 rounded-full items-center justify-center mr-3">
                <MapPin color="#3f8efc" size={20}/>
            </View>
            <View className="flex-1">
                <Text className="font-poppins-semibold text-gray-800">{item.name}</Text>
                <Text className="font-poppins-medium text-gray-600">Slot: {item.slot} • ${item.rate}/час</Text>
            </View>
            {item.id === currentParkingZone.id && (
                <View className="bg-green-100 px-3 py-1 rounded-full">
                    <Text className="font-poppins-medium text-green-700">Current</Text>
                </View>
            )}
        </TouchableOpacity>
    );

    return (
        <>
            <ScrollView className="w-full h-full bg-white">
                <LinearGradient
                    colors={['#3f8efc', '#2c70d2']}
                    className="pt-10 pb-8 rounded-b-3xl">
                    <View className={"items-center"}>
                        <View className={"mt-14 mb-4"}>
                            <Header header={"Active Session"} style={"user"} textColor={"white"}/>
                        </View>

                        {/* Анимированный круг с таймером */}
                        <Animated.View
                            className="w-80 h-80 border-8 border-white rounded-full flex items-center justify-center mt-8"
                            style={{
                                transform: [{ scale: animatedValue }],
                            }}>
                            <View>
                                <Text className="text-5xl leading-snug font-poppins-semibold text-white text-center">
                                    {timeElapsed.hours}h {timeElapsed.minutes}m
                                </Text>
                                <Text className={"text-xl font-poppins-medium text-white text-center mt-2"}>
                                    BBK2906
                                </Text>
                                <View className="mt-6 items-center">
                                    <Text className="text-white text-lg font-poppins-medium">Current cost:</Text>
                                    <Text className="text-white text-3xl font-poppins-semibold mt-1">{calculateCurrentCost()} PLN</Text>
                                </View>
                            </View>
                        </Animated.View>
                    </View>

                    <View className="w-full px-6 flex-row justify-between mt-8 mb-4">
                        <TouchableOpacity
                            className="flex-row items-center bg-white/20 px-4 py-2 rounded-xl"
                            onPress={handleChangeZone}>
                            <MapPin color="white" size={24} className="mr-2"/>
                            <Text className="ml-1 text-lg font-poppins-medium text-white">
                                Slot: {currentParkingZone.slot}
                            </Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            className="bg-white/20 px-4 py-2 rounded-xl"
                            onPress={handleAdditionalPayment}>
                            <Text className="text-lg font-poppins-medium text-white">
                                Pay
                            </Text>
                        </TouchableOpacity>
                    </View>
                </LinearGradient>

                <View className="px-5 mt-5 mb-[100px]">
                    {/* Блок с информацией об оплате */}
                    <View className="bg-white rounded-3xl p-5 shadow-md shadow-gray-300 elevation-5 mb-5">
                        <View className="mb-5 pb-2 border-b border-gray-100">
                            <Text className="text-xl font-poppins-semibold text-gray-800">Payment Information</Text>
                        </View>

                        <View className="space-y-5">
                            <View className="flex-row items-center">
                                <View className="w-10 h-10 bg-green-100 rounded-full items-center justify-center mr-3">
                                    <CheckCircle color="#22c55e" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Payed</Text>
                                    <Text className="font-poppins-semibold text-gray-800">{paidAmount.toFixed(2)} PLN</Text>
                                </View>
                            </View>

                            <View className="flex-row items-center mt-2">
                                <View className="w-10 h-10 bg-yellow-100 rounded-full items-center justify-center mr-3">
                                    <DollarSign color="#eab308" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Rest</Text>
                                    <Text className="font-poppins-semibold text-gray-800">{calculateRemainingCost()} PLN</Text>
                                </View>
                            </View>

                            <TouchableOpacity
                                className="mt-4 bg-[#3f8efc] rounded-xl py-4 items-center"
                                onPress={handleAdditionalPayment}>
                                <Text className="text-white font-poppins-semibold text-lg">Pay</Text>
                            </TouchableOpacity>
                        </View>
                    </View>

                    <View className="bg-white rounded-3xl p-5 shadow-md shadow-gray-300 elevation-5 mb-5">
                        <View className="mb-5 pb-2 border-b border-gray-100">
                            <Text className="text-xl font-poppins-semibold text-gray-800">Parking Session Details</Text>
                        </View>

                        <View className="space-y-6">
                            <TouchableOpacity
                                className="flex-row items-center"
                                onPress={handleChangeZone}>
                                <View className="w-10 h-10 bg-blue-100 rounded-full items-center justify-center mr-4">
                                    <MapPin color="#3f8efc" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Parking</Text>
                                    <Text className="font-poppins-semibold text-gray-800 mt-1">{currentParkingZone.name}</Text>
                                </View>
                                <Feather name="chevron-right" size={20} color="#9e9e9e" />
                            </TouchableOpacity>

                            <TouchableOpacity className="flex-row items-center mt-3">
                                <View className="w-10 h-10 bg-purple-100 rounded-full items-center justify-center mr-4">
                                    <Clock color="#8b5cf6" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Start time</Text>
                                    <Text className="font-poppins-semibold text-gray-800 mt-1">17:48, 8 Apr 2025</Text>
                                </View>
                            </TouchableOpacity>

                            <TouchableOpacity
                                className="flex-row items-center mt-3">
                                <View className="w-10 h-10 bg-green-100 rounded-full items-center justify-center mr-4">
                                    <Car color="#22c55e" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Vehicle</Text>
                                    <Text className="font-poppins-semibold text-gray-800 mt-1">Renault Arkana</Text>
                                </View>
                            </TouchableOpacity>

                            <TouchableOpacity className="flex-row items-center mt-3">
                                <View className="w-10 h-10 bg-yellow-100 rounded-full items-center justify-center mr-4">
                                    <DollarSign color="#eab308" size={20}/>
                                </View>
                                <View className="flex-1">
                                    <Text className="font-poppins-medium text-gray-600">Tariff</Text>
                                    <Text className="font-poppins-semibold text-gray-800 mt-1">PLN {currentParkingZone.rate}/h</Text>
                                </View>
                            </TouchableOpacity>
                        </View>

                        <TouchableOpacity
                            className="mt-8 bg-[#3f8efc] rounded-xl py-4 items-center"
                            onPress={handleEndSession}>
                            <Text className="text-white font-poppins-semibold text-lg">Pay</Text>
                        </TouchableOpacity>
                    </View>

                    <View className="mt-6 mb-8 bg-white rounded-3xl p-5 shadow-md shadow-gray-300 elevation-5">
                        <View className="mb-5">
                            <Text className="text-xl font-poppins-semibold text-gray-800">Additional options</Text>
                        </View>

                        <TouchableOpacity
                            className="flex-row items-center justify-between p-4 bg-gray-50 rounded-xl mb-4"
                            onPress={handleReportIssue}>
                            <View className="flex-row items-center">
                                <View className="w-10 h-10 bg-red-100 rounded-full items-center justify-center mr-4">
                                    <Feather name="alert-circle" size={20} color="#ef4444" />
                                </View>
                                <Text className="font-poppins-medium text-gray-800">Report a problem</Text>
                            </View>
                            <Feather name="chevron-right" size={20} color="#9e9e9e" />
                        </TouchableOpacity>

                        <TouchableOpacity
                            className="flex-row items-center justify-between p-4 bg-gray-50 rounded-xl"
                            onPress={() => Alert.alert("Infromation", "Bill succesful sent to your email")}>
                            <View className="flex-row items-center">
                                <View className="w-10 h-10 bg-blue-100 rounded-full items-center justify-center mr-4">
                                    <Feather name="file-text" size={20} color="#3f8efc" />
                                </View>
                                <Text className="font-poppins-medium text-gray-800">Receive a check</Text>
                            </View>
                            <Feather name="chevron-right" size={20} color="#9e9e9e" />
                        </TouchableOpacity>
                    </View>
                </View>
            </ScrollView>

            {/* Модальное окно выбора зоны парковки */}
            <Modal
                visible={isZoneModalVisible}
                animationType="slide"
                transparent={true}
                onRequestClose={() => setIsZoneModalVisible(false)}
            >
                <View className="flex-1 bg-black/50 justify-end">
                    <View className="bg-white rounded-t-3xl max-h-[70%]">
                        <View className="flex-row justify-between items-center p-5 border-b border-gray-100">
                            <Text className="text-xl font-poppins-semibold text-gray-800">Select a parking zone</Text>
                            <TouchableOpacity onPress={() => setIsZoneModalVisible(false)}>
                                <X size={24} color="#9e9e9e" />
                            </TouchableOpacity>
                        </View>

                        <FlatList
                            data={parkingZones}
                            renderItem={renderZoneItem}
                            keyExtractor={item => item.id}
                            className="max-h-[80%]"
                        />

                        <View className="p-4 border-t border-gray-100">
                            <TouchableOpacity
                                className="bg-gray-100 rounded-xl py-4 items-center"
                                onPress={() => setIsZoneModalVisible(false)}>
                                <Text className="font-poppins-semibold text-gray-800">Cancellation</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        </>
    )
}

export default ParkingSessionActive