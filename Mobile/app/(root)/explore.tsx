import {View, Text, ScrollView, TouchableOpacity, Image} from 'react-native';
import React, {useEffect} from 'react';
import {Bell, MapPin} from 'lucide-react-native';
import BottomMenu from "@/app/components/BottomMenu";
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import {FontAwesome5, Ionicons, MaterialCommunityIcons} from "@expo/vector-icons";
import {useState} from "react";
import api from "@/app/http/api";


type ParkingData = {
    id: string;
    name: string;
    address: string;
    imageUrl: string;
    rating: number;
    distance: string;
    price: string;
    spaces: {
        occupied: number;
        total: number;
    };
};

export  interface IOpeningHours {
    dayOfWeek: DayOfWeek;
    openingTime?: [number, number];
    closingTime?: [number, number];
    isClosed?: boolean;
}
export  interface ICapacity {
    capacity: number;
    occupiedSpaces: number;
    placesForDisabled: number;
    occupiedPlacesForDisabled: number;
    placesForElectricCars: number;
    occupiedPlacesForElectricCar: number;
}

export interface IAddress {
    id: string | undefined | null;
    countryCode: string;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
    latitude: number |null;
    longitude: number |null;
    isBelongToAnyInstitution: boolean;
    institutionName: string | undefined | null;
    recordStatus: string | undefined | null;
    creationDate: string | undefined | null;
    modificationDate: string | undefined | null;
}
export enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

export interface IParkingTag{
     id: string | undefined | null;
     name: string | undefined | null;
     description: string | undefined | null;
}

 export interface IParking {
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
// UUID id,
//     String name,
//     String imageUrl,
//     AddressDto address,
//     Integer capacity,
//     Integer occupiedSlots,
//     Boolean is24h,
// List<OpeningHoursDto> workingTimeList,
// List<ParkingTagDto> tags




const mockParkingData: ParkingData[] = [
    {
        id: "parking1",
        name: "Wi Zut Parking Spot",
        address: "Zolnerska 51, 70-953 Szczecin",
        imageUrl: "https://images.unsplash.com/photo-1741879080222-b9b5f20b3333?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxMjJ8fHxlbnwwfHx8fHw%3D",
        rating: 4,
        distance: "230 m",
        price: "26 pln/h",
        spaces: {
            occupied: 76,
            total: 256
        }
    },
    {
        id: "parking2",
        name: "City Central Parking",
        address: "Piastów 12, 70-420 Szczecin",
        imageUrl: "https://images.unsplash.com/photo-1590674899484-d5640e854abe?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
        rating: 4.5,
        distance: "450 m",
        price: "30 pln/h",
        spaces: {
            occupied: 120,
            total: 200
        }
    },
    {
        id: "parking3",
        name: "Station Parking",
        address: "Kolejowa 3, 70-200 Szczecin",
        imageUrl: "https://images.unsplash.com/photo-1506521781263-d8422e82f27a?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3",
        rating: 3.5,
        distance: "650 m",
        price: "15 pln/h",
        spaces: {
            occupied: 45,
            total: 150
        }
    }
];


const ParkingComponent = ({ parkingData }: { parkingData: IParking }) => {
    const router = useExpoRouter();

    const handleCarProfile = () => {
        router.push(`/(parking)/${parkingData.id}`);
    };


    //have not found in dto
    const rating = 5
    const distance = 200;
    const price = 5;


    // Создаем массив для отображения рейтинга
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating - fullStars >= 0.5;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    return (
        <TouchableOpacity onPress={handleCarProfile} className="w-full">
            <View className="bg-white rounded-2xl flex-row items-start shadow-md p-4 mx-5 mb-4">
                <Image
                    source={{ uri: parkingData.imageUrl }}
                    className="w-20 h-20 rounded-xl"
                />
                <View className="ml-4 flex-1">
                    <Text className="font-bold text-base">{parkingData.name}</Text>
                    <Text className="text-sm text-gray-500 mb-2">
                        {parkingData.address.street}, {parkingData.address.buildingNumber}, {parkingData.address.city}, {parkingData.address.institutionName}
                    </Text>

                    <View className="flex-col">
                        <View className="flex-row items-center">
                            {[...Array(fullStars)].map((_, index) => (
                                <Ionicons key={`full-${index}`} name="star" size={14} color="#438eff" />
                            ))}
                            {hasHalfStar && (
                                <Ionicons key="half" name="star-half" size={14} color="#438eff" />
                            )}
                            {[...Array(emptyStars)].map((_, index) => (
                                <Ionicons key={`empty-${index}`} name="star-outline" size={14} color="#438eff" />
                            ))}
                        </View>
                        <View className="flex-row items-center mt-4 gap-3">
                            <Text className="text-xs text-gray-500">{distance}</Text>
                            <Text className="text-xs text-gray-500">{price}</Text>
                            <Text className="text-xs text-gray-500">{parkingData.occupiedSlots}/{parkingData.capacity} spots</Text>
                        </View>
                    </View>
                </View>
            </View>
        </TouchableOpacity>
    );
};

export interface IDriver{
    id: string;
    userId: string;
    profilePicture: string;
    firstName: string;
    lastName: string;
    country: string;
    city: string;
    email:string;
    phoneNumber: string;
    phoneCode: string;
    birthDate: number[];
    isEmailVerified: boolean;
    isPhoneVerified: boolean;
    creationDate: number[];
    modificationDate: number[];
}
export enum AccountType {
    PERSONAL= "PERSONAL",
    BUSINESS = "BUSINESS",
    FAMILY = "FAMILY",
    SYSTEM = "SYSTEM",
}
export interface IAccountBalance {
    id:string;
    creationDate: number[];
    modificationDate: number[];
    preferredCurrency: {
        code: string;
        symbolKey:string;
    };
    balance: number;
    owenerId:string;
    accountType: AccountType;
    stripeAccountId: string;
    isVerifiedAccount: boolean;
}

export default function Explore() {
    const router = useExpoRouter();

    const handleLocation = () => {
        router.navigate("/parking-session-active")
    }


    //fetch Driver data
    const [parkingList, setParkingList] = useState<IParking[]>([] as IParking[]);
    useEffect(() => {
        const fetchData = async () => {
            try{
                console.log("Fetch parking fetch" , "53.4525613, 14.5383154")

                const response = await api.get(`/api/v1/parking/search`, {
                    params: {
                        page: 0,
                    }
                })


                console.log("REsponse parking fetch" ,response)

                if(response.status  < 210){
                    setParkingList(response.data.content)
                }



            } catch (error) {
                console.error("Failed to fetch parking:", error);
            }
        };

        fetchData();
    }, []);

    const [driver, setDriver] = useState<IDriver | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try{

                const response = await api.get(`/api/v1/drivers`)

                console.log("Response drivers" ,response)

                if(response.status  < 210){
                    setDriver(response.data)
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
        };

        fetchData();
    }, []);

    const [driverBalance, setDriverBalance] = useState<IAccountBalance>({} as IAccountBalance);

    useEffect(() => {
        const fetchData = async () => {
            try{

                const response = await api.get(`/api/v1/accounts-balance/owned/${driver?.userId}`);

                console.log("Response drivers balance" ,response)

                if(response.status  < 210){
                    setDriverBalance(response.data)
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
        };

        fetchData();
    }, [driver?.id]);

    return (
        <View className="flex-1 bg-white">
            <ScrollView className="flex-1" bounces={true}>
                {/* Верхний блок */}
                <View className="h-[320px] bg-[#438eff] px-5">
                    <View className="flex-row justify-between">
                        <TouchableOpacity
                            onPress={handleLocation}
                            className="w-44 bg-white/20 rounded-[24px] mt-24 items-center justify-center flex-row">
                            <MapPin size={20} color="#fff"/>
                            <Text className="text-white text-sm ml-2">Szczecin, Poland</Text>
                        </TouchableOpacity>

                        <TouchableOpacity
                            className="size-12 mt-24 bg-white/20 rounded-[24px] justify-center items-center"
                            onPress={() => router.push('/notifications')}>
                            <Bell size={20} color="#fff"/>
                        </TouchableOpacity>
                    </View>

                    <View className="mt-8 flex-row justify-between items-center">
                        <View className="flex-col">
                            <Text className="text-white text-xl font-light">Your balance</Text>
                            <Text className="text-white text-4xl font-semibold">{driverBalance.balance} {driverBalance.preferredCurrency}</Text>
                        </View>
                        <TouchableOpacity
                            className="bg-white/30 p-2 rounded-full"
                            onPress={() => router.navigate('/topping-up-first')}>
                            <Ionicons name="add" size={24} color="white" />
                        </TouchableOpacity>
                    </View>

                    {/* Блок с кнопками */}
                    <View className="bg-white mt-7 rounded-2xl py-4 px-3 shadow-md">
                        <View className="flex-row justify-between mb-2">
                            {[
                                { icon: "heart", label: "Saved", source: FontAwesome5, navigate: "/transactions" },
                                { icon: "charging-station", label: "Transactions", source: FontAwesome5, navigate: "/transactions" },
                                { icon: "shield-check", label: "Family Share", source: MaterialCommunityIcons, navigate: "/transactions" },
                                { icon: "clock", label: "Find Parking Spot", source: FontAwesome5, navigate: "/transactions" },
                            ].map((item, index) => {
                                const Icon = item.source;
                                return (
                                    <TouchableOpacity
                                        key={index}
                                        className="items-center w-1/4 mt-5"
                                        onPress={() => router.navigate(item.navigate)}>
                                        <View className="bg-[#EEF1FF] rounded-2xl shadow-sm w-12 h-12 justify-center items-center">
                                            <Icon
                                                name={item.icon as any}
                                                size={20}
                                                color="#438eff"
                                            />
                                        </View>
                                        <Text className="text-xs mt-2 text-gray-600 text-center">{item.label}</Text>
                                    </TouchableOpacity>
                                );
                            })}
                        </View>
                    </View>
                </View>

                {/* Nearby parking */}
                <View className="mt-[50px] px-5 mb-1">
                    <Text className="text-lg font-bold mb-1">Nearest parking lots</Text>
                    <Text className="text-sm text-gray-500">Best parking lots near you</Text>
                </View>

                {/* Список парковок */}
                {parkingList &&  <View className="py-5">
                    {parkingList.map((parking) => (
                        <ParkingComponent key={parking.id} parkingData={parking} />
                    ))}
                </View>}
            </ScrollView>

            {/*/!* Нижнее меню *!/*/}
            {/*<BottomMenu/>*/}
        </View>
    );
};