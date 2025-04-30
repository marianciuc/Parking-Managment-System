import {
    View,
    Text,
    TouchableOpacity,
    ScrollView,
    SafeAreaView,
    RefreshControl,
    ActivityIndicator,
    Dimensions,
    StatusBar
} from 'react-native';
import React, { useContext, useEffect, useState } from 'react';
import { useNavigation } from '@react-navigation/native';
import Header from '@/app/components/header';
import { useExpoRouter } from 'expo-router/build/global-state/router-store';
import IVehicle from '@/app/interfaces/IVehicle';
import { Context } from '@/app/_layout';
import {
    Plus,
    Car,
    Users,
    Filter,
    Calendar,
    ChevronRight,
    User,
    MapPin,
    Settings,
    Clock,
    AlertCircle,
    ChevronDown,
    Dot
} from 'lucide-react-native';

const { width } = Dimensions.get('window');

const EmptyState = ({ type }: { type: 'personal' | 'shared' }) => (
    <View className="py-8 items-center justify-center bg-white rounded-lg mx-4 my-2 border border-gray-100">
        {type === 'personal' ? (
            <>
                <Car size={48} color="#E2E8F0" />
                <Text className="text-base font-medium text-gray-600 mt-4">
                    No personal automobiles
                </Text>
                <Text className="text-sm text-gray-400 text-center px-10 mt-1">
                    Click the "+" button to add the Vehicle
                </Text>
            </>
        ) : (
            <>
                <Users size={48} color="#E2E8F0" />
                <Text className="text-base font-medium text-gray-600 mt-4">
                    There are no cars available
                </Text>
                <Text className="text-sm text-gray-400 text-center px-10 mt-1">
                    Access via Family Share has not been granted
                </Text>
            </>
        )}
    </View>
);

const VehicleListItem = ({ vehicle, isLast }: { vehicle: IVehicle, isLast: boolean }) => {
    const router = useExpoRouter();

    const handleCarProfile = () => {
        router.push(`/(car)/${vehicle.id}`);
    };

    // Дополнительные поля
    const lastServiceDate = vehicle.lastService || "No data";
    const mileage = vehicle.mileage || "No data";
    const isOwned = vehicle.isOwned !== undefined ? vehicle.isOwned : true;
    const ownerName = vehicle.ownerName;
    const lastParking = vehicle.lastParking || "No data";
    const vehicleStatus = vehicle.status || "Not in the parking lot";
    const isParked = vehicleStatus === "In the parking lot";
    const hasWarning = vehicle.hasWarning || false;

    return (
        <TouchableOpacity
            onPress={handleCarProfile}
            activeOpacity={0.7}
            className={`bg-white p-4 ${!isLast ? 'border-b border-gray-100' : ''}`}
        >
            <View className="flex-row justify-between items-center">
                <View className="flex-row items-center flex-1">
                    <View className={`w-10 h-10 rounded-full items-center justify-center ${isOwned ? 'bg-green-50' : 'bg-purple-50'}`}>
                        <Car size={20} color={isOwned ? "#16A34A" : "#9333EA"} />
                    </View>

                    <View className="ml-3 flex-1">
                        <View className="flex-row items-center">
                            <Text className="text-base font-semibold text-gray-900" numberOfLines={1}>
                                {vehicle.brand} {vehicle.model}
                            </Text>
                            {!isOwned && (
                                <View className="ml-2 px-2 py-[2px] bg-purple-50 rounded-full flex-row items-center">
                                    <Users size={12} color="#9333EA" />
                                    <Text className="text-[10px] text-purple-700 font-medium ml-[2px]">
                                        Family
                                    </Text>
                                </View>
                            )}
                            {hasWarning && (
                                <View className="ml-1">
                                    <AlertCircle size={16} color="#EF4444" />
                                </View>
                            )}
                        </View>

                        <View className="flex-row items-center mt-[2px]">
                            <Text className="text-sm text-gray-500 font-medium">
                                {vehicle.plateNumber}
                            </Text>
                            {isParked && (
                                <View className="flex-row items-center ml-2">
                                    <Dot size={18} color="#2563EB" />
                                    <Text className="text-xs text-blue-600">In the parking lot</Text>
                                </View>
                            )}
                        </View>
                    </View>
                </View>

                <ChevronRight size={20} color="#94A3B8" />
            </View>

            <View className="mt-2 flex-row flex-wrap">
                <View className="flex-row items-center mr-4 mt-1">
                    <MapPin size={14} color="#64748B" />
                    <Text className="text-xs text-gray-500 ml-1" numberOfLines={1}>
                        {isParked ? lastParking : "Don't have active parking session"}
                    </Text>
                </View>

                <View className="flex-row items-center mr-4 mt-1">
                    <Settings size={14} color="#64748B" />
                    <Text className="text-xs text-gray-500 ml-1">
                        29.03.2025
                    </Text>
                </View>
            </View>

            {!isOwned && ownerName && (
                <View className="mt-1 flex-row items-center">
                    <User size={14} color="#64748B" />
                    <Text className="text-xs text-gray-500 ml-1">
                        Owner: {ownerName}
                    </Text>
                </View>
            )}
        </TouchableOpacity>
    );
};

const SectionHeader = ({
                           title,
                           count,
                           onToggle,
                           isExpanded
                       }: {
    title: string;
    count: number;
    onToggle: () => void;
    isExpanded: boolean;
}) => (
    <TouchableOpacity
        onPress={onToggle}
        className="flex-row justify-between items-center py-3 px-4 bg-gray-50 border-b border-t border-gray-200"
    >
        <View className="flex-row items-center">
            {title === "My vehicles" ? (
                <Car size={18} color="#0F172A" />
            ) : (
                <Users size={18} color="#0F172A" />
            )}
            <Text className="text-base font-semibold text-gray-900 ml-2">{title}</Text>
            <View className="bg-gray-200 rounded-full px-2 py-[1px] ml-2">
                <Text className="text-xs text-gray-700 font-medium">{count}</Text>
            </View>
        </View>

        <ChevronDown size={20} color="#64748B" className={`transform ${isExpanded ? '' : 'rotate-180'}`} />
    </TouchableOpacity>
);

const OwnedCars = () => {
    const { vehicleStore, userStore } = useContext(Context);
    const [userId, setUserId] = useState('');
    const [page, setPage] = useState(vehicleStore.page);
    const [allVehicles, setAllVehicles] = useState<IVehicle[]>([]);
    const [personalVehicles, setPersonalVehicles] = useState<IVehicle[]>([]);
    const [sharedVehicles, setSharedVehicles] = useState<IVehicle[]>([]);
    const [refreshing, setRefreshing] = useState(false);
    const [loading, setLoading] = useState(true);
    const [showPersonal, setShowPersonal] = useState(true);
    const [showShared, setShowShared] = useState(true);

    const router = useExpoRouter();

    useEffect(() => {
        // if (userId) {
            loadVehicles();
        // }
    }, [userId, page]);

    useEffect(() => {
        if (userStore.user == null) {
            userStore.fetchUser().then(() => {
                if (userStore.user != null) {
                    setUserId(userStore.user.userId);
                }
            });
        } else {
            setUserId(userStore.user.userId);
        }
    }, []);

    useEffect(() => {
        // Разделяем автомобили на личные и с доступом через Family Share
        const personal = allVehicles.filter(vehicle => vehicle.isOwned !== false);
        const shared = allVehicles.filter(vehicle => vehicle.isOwned === false);

        setPersonalVehicles(personal);
        setSharedVehicles(shared);
    }, [allVehicles]);

    const loadVehicles = async () => {
        setLoading(true);
        try {
            await vehicleStore.fetchVehicle(userId, page);
            setAllVehicles(vehicleStore.vehiclePage);
        } catch (error) {
            console.error('Failed to load vehicles:', error);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    };

    const onRefresh = () => {
        setRefreshing(true);
        loadVehicles();
    };

    return (
        <SafeAreaView className="flex-1 bg-gray-100">
            <StatusBar barStyle="dark-content" backgroundColor="#fff" />
            <Header header="Vehicles" style="header" textColor="white" />

            <View className="flex-row justify-between items-center p-4 bg-white border-b border-gray-200">
                <Text className="text-base font-semibold text-gray-900">
                    {allVehicles.length} {allVehicles.length === 1 ? 'vehicle' :
                    (allVehicles.length >= 2 && allVehicles.length <= 4) ? 'vehicles' : 'vehicles'}
                </Text>
                <TouchableOpacity
                    className="flex-row items-center bg-gray-50 px-3 py-2 rounded-lg border border-gray-200"
                    onPress={() => {router.push('/add-car');}}
                >
                    <Filter size={16} color="#64748B" />
                    <Text className="ml-1 text-sm text-gray-700">Add car</Text>
                </TouchableOpacity>
            </View>

            {loading && !refreshing ? (
                <View className="flex-1 justify-center items-center bg-white">
                    <ActivityIndicator size="large" color="#3B82F6" />
                    <Text className="text-sm text-gray-500 mt-2">Loading...</Text>
                </View>
            ) : (
                <ScrollView
                    className="flex-1"
                    refreshControl={
                        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
                    }
                >
                    {/* Мои автомобили */}
                    <View className="mt-2">
                        <SectionHeader
                            title="My Vehicles"
                            count={personalVehicles.length}
                            onToggle={() => setShowPersonal(!showPersonal)}
                            isExpanded={showPersonal}
                        />

                        {showPersonal && (
                            <View className="bg-white rounded-b-lg overflow-hidden">
                                {personalVehicles.length > 0 ? (
                                    personalVehicles.map((vehicle, index) => (
                                        <VehicleListItem
                                            key={vehicle.id}
                                            vehicle={vehicle}
                                            isLast={index === personalVehicles.length - 1}
                                        />
                                    ))
                                ) : (
                                    <EmptyState type="personal" />
                                )}
                            </View>
                        )}
                    </View>

                    {/* Family Share автомобили */}
                    <View className="mt-2 mb-20">
                        <SectionHeader
                            title="Family Share"
                            count={sharedVehicles.length}
                            onToggle={() => setShowShared(!showShared)}
                            isExpanded={showShared}
                        />

                        {showShared && (
                            <View className="bg-white rounded-b-lg overflow-hidden">
                                {sharedVehicles.length > 0 ? (
                                    sharedVehicles.map((vehicle, index) => (
                                        <VehicleListItem
                                            key={vehicle.id}
                                            vehicle={vehicle}
                                            isLast={index === sharedVehicles.length - 1}
                                        />
                                    ))
                                ) : (
                                    <EmptyState type="shared" />
                                )}
                            </View>
                        )}
                    </View>
                </ScrollView>
            )}

            <TouchableOpacity
                className="absolute bottom-6 right-6 bg-blue-600 w-14 h-14 rounded-full items-center justify-center shadow-lg"
                onPress={() => router.push('/add-car')}
                activeOpacity={0.8}
            >
                <Plus color="#fff" size={24} />
            </TouchableOpacity>
        </SafeAreaView>
    );
};

export default OwnedCars;