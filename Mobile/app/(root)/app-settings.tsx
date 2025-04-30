import { View, Text, SafeAreaView, TouchableOpacity, ScrollView, Switch, Alert } from 'react-native'
import React, { useContext, useState } from 'react'
import { Context } from "@/app/_layout";
import { useExpoRouter } from "expo-router/build/global-state/router-store";
import { ChevronLeft, ChevronRight, BellRing, Shield, Globe, Moon, Smartphone,
    LogOut, Trash2, HelpCircle, FileText, MailOpen, ShieldCheck } from "lucide-react-native";
import * as Application from 'expo-application';
import Constants from 'expo-constants';

const AppSettings = () => {
    const router = useExpoRouter();
    const { userStore } = useContext(Context);

    // Получаем информацию о версии приложения
    const appVersion = Constants.expoConfig?.version || '1.0.0';
    const buildNumber = Constants.expoConfig?.ios?.buildNumber || Constants.expoConfig?.android?.versionCode || '1';

    // Состояния для переключателей
    const [notificationsEnabled, setNotificationsEnabled] = useState(true);
    const [marketingNotificationsEnabled, setMarketingNotificationsEnabled] = useState(false);
    const [darkModeEnabled, setDarkModeEnabled] = useState(false);
    const [biometricsEnabled, setBiometricsEnabled] = useState(true);
    const [autoPlayEnabled, setAutoPlayEnabled] = useState(true);
    const [locationTrackingEnabled, setLocationTrackingEnabled] = useState(true);

    // Функция для обработки выхода из аккаунта
    const handleLogout = () => {
        Alert.alert(
            "Logging out of the account",
            "Are you sure you want to log out of your account?",
            [
                { text: "Отмена", style: "cancel" },
                {
                    text: "Выйти",
                    onPress: () => {
                        // Здесь будет логика выхода из аккаунта
                        console.log("Logging out of the account");
                        // После успешного выхода перенаправляем на страницу авторизации
                        router.push('/auth/login');
                    }
                }
            ]
        );
    };

    // Функция для обработки удаления аккаунта
    const handleDeleteAccount = () => {
        Alert.alert(
            "Account Deletion",
            "Are you sure you want to delete your account? This action is irreversible and all your data will be deleted.",
            [
                { text: "Cancel", style: "cancel" },
                {
                    text: "Delete account",
                    style: "destructive",
                    onPress: () => {
                        // Второй уровень подтверждения
                        Alert.alert(
                            "Confirming deletion",
                            "Please confirm that you understand: all your data and history will be deleted without possibility of recovery.",
                            [
                                { text: "Cancel", style: "cancel" },
                                {
                                    text: "Confirm deletion",
                                    style: "destructive",
                                    onPress: () => {
                                        // Здесь будет логика удаления аккаунта
                                        console.log("Account deletion");
                                        // После успешного удаления перенаправляем на страницу авторизации
                                        router.push('/auth/login');
                                    }
                                }
                            ]
                        );
                    }
                }
            ]
        );
    };

    // Функция для очистки кеша
    const handleClearCache = () => {
        Alert.alert(
            "Clearing the cache",
            "Do you want to clear the application cache? This may take some time.",
            [
                { text: "Cancel", style: "cancel" },
                {
                    text: "Clear",
                    onPress: () => {
                        // Имитация очистки кеша
                        Alert.alert("Done", "Application cache cleared");
                    }
                }
            ]
        );
    };

    return (
        <ScrollView className="flex-1 bg-gray-50">
            <SafeAreaView>
                {/* Заголовок с кнопкой назад */}
                <View className="flex-row items-center px-4 pt-4 pb-3 bg-white border-b border-gray-100">
                    <TouchableOpacity
                        onPress={() => router.back()}
                        className="flex-row items-center"
                    >
                        <ChevronLeft size={20} color="#3f8efc" />
                        <Text className="text-[#3f8efc] ml-1">Go back</Text>
                    </TouchableOpacity>

                    <Text className="text-lg font-semibold text-gray-900 mx-auto">Settings</Text>

                    <View style={{ width: 60 }} />
                </View>

                {/* Секция уведомлений */}
                <View className="bg-white mb-2">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Notifications</Text>
                    </View>

                    <View className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100">
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-blue-100 items-center justify-center">
                                <BellRing size={18} color="#3f8efc" />
                            </View>
                            <Text className="text-gray-900 ml-3">Push Notifications</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={notificationsEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={setNotificationsEnabled}
                            value={notificationsEnabled}
                        />
                    </View>

                    <View className="flex-row justify-between items-center px-5 py-4">
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-blue-100 items-center justify-center">
                                <MailOpen size={18} color="#3f8efc" />
                            </View>
                            <Text className="text-gray-900 ml-3">Marketing notifications</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={marketingNotificationsEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={setMarketingNotificationsEnabled}
                            value={marketingNotificationsEnabled}
                        />
                    </View>
                </View>

                {/* Секция внешнего вида */}
                <View className="bg-white mb-2">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Appereance</Text>
                    </View>

                    <View className="flex-row justify-between items-center px-5 py-4">
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-gray-100 items-center justify-center">
                                <Moon size={18} color="#6b7280" />
                            </View>
                            <Text className="text-gray-900 ml-3">Dark mode</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={darkModeEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={setDarkModeEnabled}
                            value={darkModeEnabled}
                        />
                    </View>
                </View>

                {/* Секция безопасности */}
                <View className="bg-white mb-2">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Security</Text>
                    </View>

                    <TouchableOpacity
                        onPress={() => router.push('/settings/privacy')}
                        className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-green-100 items-center justify-center">
                                <Shield size={18} color="#16a34a" />
                            </View>
                            <Text className="text-gray-900 ml-3">Privacy and Secutiry</Text>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>

                    <View className="flex-row justify-between items-center px-5 py-4">
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-green-100 items-center justify-center">
                                <ShieldCheck size={18} color="#16a34a" />
                            </View>
                            <Text className="text-gray-900 ml-3">Use FaceID</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={biometricsEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={setBiometricsEnabled}
                            value={biometricsEnabled}
                        />
                    </View>
                </View>

                {/* Настройки приложения */}
                <View className="bg-white mb-2">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Application settings</Text>
                    </View>

                    <TouchableOpacity
                        onPress={() => router.push('/settings/language')}
                        className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-purple-100 items-center justify-center">
                                <Globe size={18} color="#8b5cf6" />
                            </View>
                            <View>
                                <Text className="text-gray-900 ml-3">Language</Text>
                                <Text className="text-gray-500 text-sm ml-3">Eng</Text>
                            </View>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>

                    <View className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100">
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-purple-100 items-center justify-center">
                                <Globe size={18} color="#8b5cf6" />
                            </View>
                            <Text className="text-gray-900 ml-3">Detect location</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={locationTrackingEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={setLocationTrackingEnabled}
                            value={locationTrackingEnabled}
                        />
                    </View>

                    <TouchableOpacity
                        onPress={handleClearCache}
                        className="flex-row justify-between items-center px-5 py-4"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-purple-100 items-center justify-center">
                                <Trash2 size={18} color="#8b5cf6" />
                            </View>
                            <Text className="text-gray-900 ml-3">Clear cache</Text>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>
                </View>

                {/* Справка и поддержка */}
                <View className="bg-white mb-2">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Support</Text>
                    </View>

                    <TouchableOpacity
                        onPress={() => router.push('/support/help')}
                        className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-amber-100 items-center justify-center">
                                <HelpCircle size={18} color="#d97706" />
                            </View>
                            <Text className="text-gray-900 ml-3">Support center</Text>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={() => router.push('/support/contact')}
                        className="flex-row justify-between items-center px-5 py-4 border-b border-gray-100"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-amber-100 items-center justify-center">
                                <MailOpen size={18} color="#d97706" />
                            </View>
                            <Text className="text-gray-900 ml-3">Contact with support</Text>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={() => router.push('/support/terms')}
                        className="flex-row justify-between items-center px-5 py-4"
                    >
                        <View className="flex-row items-center">
                            <View className="w-8 h-8 rounded-full bg-amber-100 items-center justify-center">
                                <FileText size={18} color="#d97706" />
                            </View>
                            <Text className="text-gray-900 ml-3">Terms of use</Text>
                        </View>
                        <ChevronRight size={18} color="#9ca3af" />
                    </TouchableOpacity>
                </View>

                {/* Информация о приложении */}
                <View className="bg-white mb-2">
                    <View className="items-center py-5">
                        <Text className="text-gray-500 text-sm">Version: {appVersion} ({buildNumber})</Text>
                    </View>
                </View>

                {/* Управление аккаунтом */}
                <View className="bg-white mb-10">
                    <View className="px-5 py-3 border-b border-gray-100">
                        <Text className="text-gray-900 font-medium">Account</Text>
                    </View>

                    <TouchableOpacity
                        onPress={handleLogout}
                        className="flex-row items-center px-5 py-4 border-b border-gray-100"
                    >
                        <View className="w-8 h-8 rounded-full bg-orange-100 items-center justify-center">
                            <LogOut size={18} color="#f97316" />
                        </View>
                        <Text className="text-orange-600 ml-3">Logout</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={handleDeleteAccount}
                        className="flex-row items-center px-5 py-4"
                    >
                        <View className="w-8 h-8 rounded-full bg-red-100 items-center justify-center">
                            <Trash2 size={18} color="#ef4444" />
                        </View>
                        <Text className="text-red-600 ml-3">Delete account</Text>
                    </TouchableOpacity>
                </View>
            </SafeAreaView>
        </ScrollView>
    )
}

export default AppSettings