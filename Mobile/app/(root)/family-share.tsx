import { View, Text, SafeAreaView, TouchableOpacity, ScrollView, Image, Switch, TextInput, Alert } from 'react-native'
import React, { useContext, useState } from 'react'
import { Context } from "@/app/_layout";
import { useExpoRouter } from "expo-router/build/global-state/router-store";
import { ChevronLeft, Plus, Trash2, Mail, User, Check, X } from "lucide-react-native";
import images from "@/constants/images";

const FamilyShare = () => {
    const router = useExpoRouter();
    const { userStore } = useContext(Context);

    // Состояние для хранения списка семейных аккаунтов
    const [familyMembers, setFamilyMembers] = useState([
        { id: '1', name: 'Анна Боб', email: 'anna@gmail.com', status: 'active' },
        { id: '2', name: 'Иван Боб', email: 'ivan@gmail.com', status: 'pending' }
    ]);

    // Состояние для отслеживания активности семейного доступа
    const [familySharingEnabled, setFamilySharingEnabled] = useState(true);

    // Состояния для добавления нового члена семьи
    const [showAddForm, setShowAddForm] = useState(false);
    const [newMemberEmail, setNewMemberEmail] = useState('');
    const [newMemberName, setNewMemberName] = useState('');

    // Максимальное количество членов семьи
    const maxFamilyMembers = 5;

    // Обработка включения/выключения семейного доступа
    const toggleFamilySharing = () => {
        if (familySharingEnabled) {
            // Показать предупреждение перед отключением
            Alert.alert(
                "Отключить семейный доступ?",
                "Все члены вашей семьи потеряют доступ к сервису. Вы уверены?",
                [
                    { text: "Отмена", style: "cancel" },
                    {
                        text: "Отключить",
                        style: "destructive",
                        onPress: () => setFamilySharingEnabled(false)
                    }
                ]
            );
        } else {
            setFamilySharingEnabled(true);
        }
    };

    // Добавление нового члена семьи
    const addFamilyMember = () => {
        // Валидация
        if (!newMemberEmail.trim()) {
            Alert.alert("Ошибка", "Введите email");
            return;
        }

        if (!newMemberEmail.includes('@')) {
            Alert.alert("Ошибка", "Введите корректный email");
            return;
        }

        // Проверка на максимальное количество членов семьи
        if (familyMembers.length >= maxFamilyMembers) {
            Alert.alert("Ограничение", `Максимальное количество членов семьи: ${maxFamilyMembers}`);
            return;
        }

        // Проверка на дубликаты
        if (familyMembers.some(member => member.email === newMemberEmail.trim())) {
            Alert.alert("Ошибка", "Этот email уже добавлен");
            return;
        }

        // Добавление нового члена
        const newMember = {
            id: Date.now().toString(),
            name: newMemberName.trim() || 'Пользователь',
            email: newMemberEmail.trim(),
            status: 'pending'
        };

        setFamilyMembers([...familyMembers, newMember]);
        setNewMemberEmail('');
        setNewMemberName('');
        setShowAddForm(false);

        // Имитация отправки приглашения
        Alert.alert("Приглашение отправлено", `Приглашение успешно отправлено на ${newMemberEmail}`);
    };

    // Удаление члена семьи
    const removeFamilyMember = (id) => {
        Alert.alert(
            "Удаление доступа",
            "Вы уверены, что хотите удалить доступ для этого пользователя?",
            [
                { text: "Отмена", style: "cancel" },
                {
                    text: "Удалить",
                    style: "destructive",
                    onPress: () => {
                        setFamilyMembers(familyMembers.filter(member => member.id !== id));
                    }
                }
            ]
        );
    };

    // Отменить форму добавления
    const cancelAddForm = () => {
        setShowAddForm(false);
        setNewMemberEmail('');
        setNewMemberName('');
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
                        <Text className="text-[#3f8efc] ml-1">Назад</Text>
                    </TouchableOpacity>

                    <Text className="text-lg font-semibold text-gray-900 mx-auto">Семейный доступ</Text>

                    <View style={{ width: 60 }} />
                </View>

                {/* Секция переключения семейного доступа */}
                <View className="bg-white mb-2">
                    <View className="flex-row justify-between items-center px-5 py-4">
                        <View>
                            <Text className="text-gray-900 font-semibold text-base">Семейный доступ</Text>
                            <Text className="text-gray-500 text-sm mt-1">Поделитесь доступом с членами семьи</Text>
                        </View>
                        <Switch
                            trackColor={{ false: "#e5e7eb", true: "#dbeafe" }}
                            thumbColor={familySharingEnabled ? "#3f8efc" : "#9ca3af"}
                            onValueChange={toggleFamilySharing}
                            value={familySharingEnabled}
                        />
                    </View>
                </View>

                {familySharingEnabled && (
                    <>
                        {/* Информация о семейном доступе */}
                        <View className="bg-white mb-2 px-5 py-4">
                            <Text className="text-gray-900 font-medium">О семейном доступе</Text>
                            <Text className="text-gray-500 text-sm mt-2">
                                Вы можете предоставить доступ к вашему аккаунту до {maxFamilyMembers} членам семьи.
                                Каждый член семьи получит персональный доступ, но будет использовать вашу подписку.
                            </Text>
                        </View>

                        {/* Список членов семьи */}
                        <View className="bg-white mb-2">
                            <View className="px-5 py-3 border-b border-gray-100">
                                <Text className="text-gray-900 font-medium">Члены семьи</Text>
                                <Text className="text-gray-500 text-sm mt-1">
                                    {familyMembers.length} из {maxFamilyMembers} доступных
                                </Text>
                            </View>

                            {/* Основной пользователь */}
                            <View className="flex-row items-center justify-between px-5 py-3 border-b border-gray-100">
                                <View className="flex-row items-center">
                                    <Image source={images.avatar} className="w-10 h-10 rounded-full" />
                                    <View className="ml-3">
                                        <Text className="text-gray-900 font-medium">Bob Boboa</Text>
                                        <Text className="text-gray-500 text-sm">bob@gmail.com (вы)</Text>
                                    </View>
                                </View>
                                <View className="bg-gray-100 px-2 py-1 rounded">
                                    <Text className="text-gray-700 text-xs">Владелец</Text>
                                </View>
                            </View>

                            {/* Список членов семьи */}
                            {familyMembers.map(member => (
                                <View key={member.id} className="flex-row items-center justify-between px-5 py-3 border-b border-gray-100">
                                    <View className="flex-row items-center">
                                        <View className="w-10 h-10 rounded-full bg-gray-200 items-center justify-center">
                                            <User size={20} color="#6b7280" />
                                        </View>
                                        <View className="ml-3">
                                            <Text className="text-gray-900 font-medium">{member.name}</Text>
                                            <Text className="text-gray-500 text-sm">{member.email}</Text>
                                        </View>
                                    </View>
                                    <View className="flex-row items-center">
                                        {member.status === 'pending' ? (
                                            <View className="bg-amber-100 px-2 py-1 rounded mr-2">
                                                <Text className="text-amber-700 text-xs">Ожидание</Text>
                                            </View>
                                        ) : (
                                            <View className="bg-green-100 px-2 py-1 rounded mr-2">
                                                <Text className="text-green-700 text-xs">Активен</Text>
                                            </View>
                                        )}
                                        <TouchableOpacity
                                            onPress={() => removeFamilyMember(member.id)}
                                            className="p-2"
                                        >
                                            <Trash2 size={18} color="#ef4444" />
                                        </TouchableOpacity>
                                    </View>
                                </View>
                            ))}

                            {/* Форма добавления нового члена семьи */}
                            {showAddForm ? (
                                <View className="px-5 py-4 border-b border-gray-100">
                                    <View className="mb-3">
                                        <Text className="text-gray-500 text-sm mb-2">Имя (необязательно)</Text>
                                        <TextInput
                                            className="bg-white border border-gray-200 rounded-xl px-4 py-3 text-gray-900"
                                            value={newMemberName}
                                            onChangeText={setNewMemberName}
                                            placeholder="Введите имя"
                                        />
                                    </View>

                                    <View className="mb-4">
                                        <Text className="text-gray-500 text-sm mb-2">Email (обязательно)</Text>
                                        <TextInput
                                            className="bg-white border border-gray-200 rounded-xl px-4 py-3 text-gray-900"
                                            value={newMemberEmail}
                                            onChangeText={setNewMemberEmail}
                                            placeholder="Введите email"
                                            keyboardType="email-address"
                                            autoCapitalize="none"
                                        />
                                    </View>

                                    <View className="flex-row justify-end">
                                        <TouchableOpacity
                                            onPress={cancelAddForm}
                                            className="flex-row items-center mr-4"
                                        >
                                            <X size={16} color="#6b7280" />
                                            <Text className="text-gray-600 ml-1">Отмена</Text>
                                        </TouchableOpacity>

                                        <TouchableOpacity
                                            onPress={addFamilyMember}
                                            className="flex-row items-center bg-[#3f8efc] px-4 py-2 rounded-lg"
                                        >
                                            <Check size={16} color="#fff" />
                                            <Text className="text-white ml-1">Добавить</Text>
                                        </TouchableOpacity>
                                    </View>
                                </View>
                            ) : (
                                // Кнопка для показа формы добавления
                                familyMembers.length < maxFamilyMembers && (
                                    <TouchableOpacity
                                        onPress={() => setShowAddForm(true)}
                                        className="flex-row items-center px-5 py-4"
                                    >
                                        <View className="w-10 h-10 rounded-full bg-[#dbeafe] items-center justify-center">
                                            <Plus size={20} color="#3f8efc" />
                                        </View>
                                        <Text className="text-[#3f8efc] font-medium ml-3">Добавить члена семьи</Text>
                                    </TouchableOpacity>
                                )
                            )}
                        </View>

                        {/* Дополнительная информация */}
                        <View className="bg-white px-5 py-4 mb-10">
                            <Text className="text-gray-900 font-medium mb-2">Как это работает?</Text>
                            <Text className="text-gray-500 text-sm mb-4">
                                1. Добавьте email члена вашей семьи.
                            </Text>
                            <Text className="text-gray-500 text-sm mb-4">
                                2. Мы отправим приглашение на указанный email.
                            </Text>
                            <Text className="text-gray-500 text-sm mb-4">
                                3. После принятия приглашения, член семьи получит доступ к вашей подписке.
                            </Text>
                            <Text className="text-gray-500 text-sm">
                                Вы можете отменить доступ в любое время.
                            </Text>
                        </View>
                    </>
                )}

                {!familySharingEnabled && (
                    <View className="bg-white px-5 py-10 items-center justify-center mb-4">
                        <Image source={images.avatar} className="w-16 h-16 opacity-50 mb-4" />
                        <Text className="text-gray-900 font-medium text-lg mb-2">Семейный доступ отключен</Text>
                        <Text className="text-gray-500 text-center mb-6">
                            Включите семейный доступ, чтобы делиться вашей подпиской с членами семьи.
                        </Text>
                        <TouchableOpacity
                            onPress={toggleFamilySharing}
                            className="bg-[#3f8efc] px-6 py-3 rounded-xl"
                        >
                            <Text className="text-white font-medium">Включить</Text>
                        </TouchableOpacity>
                    </View>
                )}
            </SafeAreaView>
        </ScrollView>
    )
}

export default FamilyShare