import {View, Text, ScrollView, TouchableOpacity} from 'react-native';
import React from 'react';
import {SafeAreaView} from 'react-native-safe-area-context';
import {Car, Clock, CreditCard, Bell} from 'lucide-react-native';
import {useNavigation} from '@react-navigation/native';
import Header from '@/app/components/header';

// Тип уведомления
const NotificationType = {
    PARKING: 'parking',
    SESSION: 'session',
    BALANCE: 'balance',
    OTHER: 'other'
};

const NotificationCard = ({text, time, title, type = NotificationType.OTHER}) => {
    // Определение иконки и цвета фона на основе типа уведомления
    const getIconAndBg = () => {
        switch(type) {
            case NotificationType.PARKING:
                return {
                    bg: 'bg-[#EAF3FF]',
                    icon: <Car size={24} color="#3F8EFC"/>
                };
            case NotificationType.SESSION:
                return {
                    bg: 'bg-[#E9FFF4]',
                    icon: <Clock size={24} color="#34C77C"/>
                };
            case NotificationType.BALANCE:
                return {
                    bg: 'bg-[#FFF4EA]',
                    icon: <CreditCard size={24} color="#FF9838"/>
                };
            default:
                return {
                    bg: 'bg-[#F0E9FF]',
                    icon: <Bell size={24} color="#8B3DFF"/>
                };
        }
    };

    const {bg, icon} = getIconAndBg();

    return (
        <TouchableOpacity activeOpacity={0.85}>
            <View
                className="bg-white p-4 rounded-2xl mb-4 flex-row items-start gap-3 shadow-[0_4px_12px_rgba(0,0,0,0.05)]">

                {/* Иконка */}
                <View className={`${bg} p-3 rounded-xl`}>
                    {icon}
                </View>

                {/* Контент */}
                <View className="flex-1">
                    <View className="flex-row justify-between items-center">
                        <Text className="text-black text-base font-semibold">{title}</Text>
                        <Text className="text-gray-400 text-xs">{time}</Text>
                    </View>
                    <Text className="text-gray-700 text-sm mt-1 leading-relaxed">{text}</Text>
                </View>
            </View>
        </TouchableOpacity>
    );
};

const Notifications = () => {
    const navigation = useNavigation();

    // Примеры различных типов уведомлений
    const notificationExamples = [
        {
            type: NotificationType.PARKING,
            title: "Parking Alert",
            time: "14:56",
            text: "A parking spot has opened up 200 meters from your location. Book now before it's gone!"
        },
        {
            type: NotificationType.SESSION,
            title: "Session Started",
            time: "13:42",
            text: "Your parking session has started at Downtown Parking Lot. Duration: 2 hours."
        },
        {
            type: NotificationType.BALANCE,
            title: "Balance Updated",
            time: "11:20",
            text: "Your account has been topped up with $25. Current balance: $42.50"
        },
        {
            type: NotificationType.SESSION,
            title: "Session Reminder",
            time: "09:15",
            text: "Your parking session will expire in 15 minutes. Would you like to extend it?"
        },
        {
            type: NotificationType.PARKING,
            title: "Parking Recommendation",
            time: "08:30",
            text: "Based on your destination, we recommend Central Parking Garage with 15 available spots."
        },
        {
            type: NotificationType.BALANCE,
            title: "Payment Processed",
            time: "Yesterday",
            text: "Your payment of $12.50 for parking on Main Street has been processed successfully."
        }
    ];

    return (
        <SafeAreaView className="flex-1 bg-[#F9FAFB]">
            <Header header="Notifications" style="header"/>

            <ScrollView className="px-4 pt-4" bounces={false}>
                <Text className="text-xl font-semibold text-black mb-4">Today</Text>

                {/* Разнообразные уведомления */}
                {notificationExamples.map((notification, i) => (
                    <NotificationCard
                        key={i}
                        type={notification.type}
                        title={notification.title}
                        time={notification.time}
                        text={notification.text}
                    />
                ))}
            </ScrollView>
        </SafeAreaView>
    );
};

export default Notifications;