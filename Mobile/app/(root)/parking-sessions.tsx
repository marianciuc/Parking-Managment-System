import {View, Text, TouchableOpacity, ScrollView, Image, SafeAreaView} from 'react-native'
import React, {useContext, useEffect, useState} from 'react'
import {Car} from "lucide-react-native";
import {useNavigation} from "@react-navigation/native";
import images from "@/constants/images";
import {Context} from "@/app/_layout";
import {ISessionList} from '../interfaces';
import PrettyDate from "@/app/components/parsing-date";
import Header from "@/app/components/header";
import {Feather, Ionicons} from "@expo/vector-icons";
import BottomMenu from "@/app/components/BottomMenu";
import {useExpoRouter} from "expo-router/build/global-state/router-store";

/*carnum*/
// Мок-данные для списка сессий парковки
const mockSessionList: ISessionList[] = [
    {
        id: "1",
        parkingName: "Galaxy Parking",
        location: "Szczecin, Poland",
        imageUrl: "https://images.unsplash.com/photo-1742197062761-1e44b0d7fd2e?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        cost: 2.80,
        rating: 4,
        startTime: [2023, 8, 25, 12, 20, 0], // Год, месяц, день, час, минута
        endTime: [2023, 8, 25, 14, 30],
        plateNumber: "ABC 123",
        paymentStatus: "Paid"
    },
    {
        id: "2",
        parkingName: "Central City Parking",
        location: "Warsaw, Poland",
        imageUrl: "https://images.unsplash.com/photo-1743065272129-5e261bad0c46?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxM3x8fGVufDB8fHx8fA%3D%3D",
        cost: 5.50,
        rating: 5,
        startTime: [2023, 8, 24, 9, 15, 0],
        endTime: [2023, 8, 24, 11, 45],
        plateNumber: "DEF 456",
        paymentStatus: "Paid"
    },
    {
        id: "3",
        parkingName: "Park & Go",
        location: "Krakow, Poland",
        imageUrl: "https://images.unsplash.com/photo-1743155597790-4e25ccef0be5?q=80&w=2018&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        cost: 3.25,
        rating: 3,
        startTime: [2023, 8, 23, 15, 0, 0],
        endTime: [2023, 8, 23, 18, 30, 0],
        plateNumber: "GHI 789",
        paymentStatus: "Pending"
    },
    {
        id: "4",
        parkingName: "Smart Parking",
        location: "Gdansk, Poland",
        imageUrl: "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        cost: 4.15,
        rating: 4,
        startTime: [2023, 8, 22, 11, 10, 0],
        endTime: [2023, 8, 22, 13, 45, 0],
        plateNumber: "JKL 012",
        paymentStatus: "Failed"
    },
    {
        id: "5",
        parkingName: "EasyPark",
        location: "Wroclaw, Poland",
        imageUrl: "https://images.unsplash.com/photo-1573348722427-f1d6819fdf98?q=80&w=2674&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        cost: 2.95,
        rating: 2,
        startTime: [2023, 8, 21, 8, 30, 0],
        endTime: [2023, 8, 21, 10, 0, false],
        plateNumber: "MNO 345",
        paymentStatus: "Paid"
    }
];


const calculateDuration = (startTime: Array<number>, endTime: Array<number>): string => {
    const startDate = new Date(startTime[0], startTime[1] - 1, startTime[2], startTime[3], startTime[4]);
    const endDate = new Date(endTime[0], endTime[1] - 1, endTime[2], endTime[3], endTime[4]);

    const diffInMilliseconds = endDate.getTime() - startDate.getTime();

    if (diffInMilliseconds < 0) {
        return "Invalid time range";
    }

    const diffInMinutes = Math.floor(diffInMilliseconds / (1000 * 60)); // В минутах
    const hours = Math.floor(diffInMinutes / 60); // Часы
    const minutes = diffInMinutes % 60; // Минуты

    return `${hours} h ${minutes} min`;
};

const ActiveSessionComponent = ({sessionList}: { sessionList: ISessionList }) => {

    console.log(sessionList);

    return (
        <View className="w-full p-4 bg-white rounded-xl mb-4 shadow-[0px_0px_14px_0px_rgba(0,0,0,0.15)]">
            <View className={"flex-row items-center justify-between"}>
                <View className="flex-row items-center justify-between">
                    <View className="pr-3">
                        <Car size={32} color={"#000000"}/>
                    </View>

                    <View className={"flex-col items-start pr-4"}>
                        <Text className="font-poppins-light text-lg">Renault Arkana</Text>
                        <Text className="text-2xl font-poppins-medium">{sessionList.plateNumber}</Text>
                    </View>
                </View>
                <View className={"flex-col items-end justify-between"}>
                    <Text className="font-poppins-light text-lg text-right">Duration:</Text>
                    <Text
                        className="text-2xl font-poppins-medium"> {calculateDuration(sessionList.startTime, sessionList.endTime)}</Text>
                </View>
            </View>
            <View className="flex-row items-center justify-between mt-8 mb-4">
                <View>
                    <Text className="font-poppins-light text-sm mb-1">Entry time</Text>
                    <Text className="font-medium"><PrettyDate timestamp={sessionList.startTime}/></Text>
                </View>
                <View>
                    <Text className="font-poppins-light text-sm mb-1">Leave time</Text>
                    <Text className="font-medium"><PrettyDate timestamp={sessionList.endTime}/> </Text>
                </View>
            </View>
            <Text className="font-poppins-light text-sm mb-1">Payment status</Text>
            <Text className="font-medium">{sessionList.paymentStatus}</Text>

        </View>
    )
}


const ParkingSessions = () => {
    const {sessionStore, userStore} = useContext(Context);
    const [activeTab, setActiveTab] = useState('active');
    const [userId, setUserId] = useState("");
    const [page, setPage] = useState(sessionStore.page);
    const [sessionList, setSessionList] = useState<ISessionList[]>([]);

    const router = useExpoRouter();

    const handleActiveSession = (id) => {
        router.navigate(`(session)/${id}`);
    }

    useEffect(() => {
        if (userId) {
            sessionStore.fetchSessions(userId, page).then(() => {
                setSessionList(sessionStore.sessionPage);
                console.log("Sessionlist result: " + sessionStore.sessionPage);
            })
        }
    }, [userId, page])

    useEffect(() => {
        if (userStore.user == null) {
            userStore.fetchUser().then(() => {
                if (userStore.user != null) {
                    setUserId(userStore.user?.userId);
                }
            })
        } else {
            setUserId(userStore.user?.userId);
        }
    }, [])


    return (
        <SafeAreaView className="flex-1 bg-white px-4">
            <ScrollView showsVerticalScrollIndicator={false}>
                <Header header={"Parking sessions"} style={"header"}/>

                {/* Active Session */}
                <View className="px-5 mb-5">
                    <Text className="text-s text-gray-500">Active session</Text>
                </View>
                <View className="bg-white rounded-2xl p-5 shadow shadow-blue-100 p-4 mb-4 ml-5 mr-5">
                    <View className="flex-row items-center mb-3">
                        <Image
                            source={{uri: "https://images.unsplash.com/photo-1742197062761-1e44b0d7fd2e?q=80&w=2564&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"}}
                            className="w-16 h-16 rounded-xl"
                        />
                        <View className="ml-4 flex-1">
                            <Text className="text-lg font-bold text-gray-800">Wi ZUT Parking Area</Text>
                            <Text className="text-sm text-gray-500">BBK2906</Text>
                        </View>
                    </View>

                    <View className="flex-row justify-between mt-2">
                        <View>
                            <Text className="text-xs text-gray-400">Time</Text>
                            <Text className="font-semibold text-base text-gray-800">03:56:32</Text>
                        </View>
                        <View>
                            <Text className="text-xs text-gray-400">Fee</Text>
                            <Text className="font-semibold text-base text-gray-800">5.64 PLN</Text>
                        </View>
                        <View>
                            <Text className="text-xs text-gray-400">Started</Text>
                            <Text className="font-semibold text-base text-gray-800">08:02</Text>
                        </View>
                    </View>

                    <TouchableOpacity className="bg-[#5A8CFF] mt-6 py-3 rounded-2xl" onPress={() => {
                        handleActiveSession("123")
                    }}>
                        <Text className="text-center text-white font-semibold">Details</Text>
                    </TouchableOpacity>
                </View>

                {/* History */}
                <View className="px-5 mb-5">
                    <Text className="text-s text-gray-500">Active session</Text>
                </View>
                {mockSessionList.map((session, index) => (
                    <View
                        key={session.id || index}
                        className="bg-white rounded-2xl flex-row flex items-center justify-between shadow shadow-blue-100 p-4 mb-4 ml-5 mr-5"
                    >
                        <View className="flex-row items-center">
                            <Image
                                source={{uri: session.imageUrl}}
                                className="w-12 h-12 rounded-lg"
                            />
                            <View className="ml-3">
                                <Text className="font-semibold text-gray-800">{session.plateNumber || "Unknown Parking"}</Text>
                                <Text className="text-xs text-gray-500">{session.parkingName}, {session.location || "No location data"}</Text>
                            </View>
                        </View>

                        <View className="items-end">
                            <Text className="text-sm font-semibold text-gray-800">${session.cost?.toFixed(2) || "0.00"}</Text>
                            <View className="flex-row items-center mt-1">
                                {[...Array(5)].map((_, i) => (
                                    <Ionicons
                                        key={i}
                                        name={i < (session.rating || 0) ? "star" : "star-outline"}
                                        size={12}
                                        color="#FFD700"
                                    />
                                ))}
                            </View>
                            <Text className="text-xs text-gray-400 mt-1">
                                {session.startTime ? (
                                    <PrettyDate timestamp={session.startTime} />
                                ) : (
                                    "No date"
                                )}

                            </Text>
                        </View>
                    </View>
                ))}

            </ScrollView>
        </SafeAreaView>
    )
}
export default ParkingSessions
