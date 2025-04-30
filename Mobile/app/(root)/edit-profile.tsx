import { View, Text, SafeAreaView, TouchableOpacity, ScrollView, TextInput, Platform } from 'react-native'
import React, { useContext, useState } from 'react'
import { Context } from "@/app/_layout";
import { useExpoRouter } from "expo-router/build/global-state/router-store";
import { ChevronLeft, Save, Eye, EyeOff, ChevronDown } from "lucide-react-native";
import { Picker } from '@react-native-picker/picker';

const EditProfile = () => {
    const router = useExpoRouter();
    const { userStore } = useContext(Context);

    // Список стран для выбора
    const countries = [
        { label: "Choose the country", value: "" },
        { label: "Poland", value: "Poland" },
        { label: "Germany", value: "Germany" },
        { label: "USA", value: "USA" },
        { label: "Canada", value: "Canada" },
        { label: "United Kingdom", value: "United Kingdom" },
        { label: "France", value: "France" },
        { label: "Spain", value: "Spain" },
        { label: "Italy", value: "Italy" },
        { label: "China", value: "China" },
        { label: "Japan", value: "Japan" }
    ];

    // Заполняем начальные данные из хранилища (заглушка)
    const [formData, setFormData] = useState({
        firstName: 'Vladimir',
        lastName: 'Marianciuc',
        phone: '+48516023312',
        location: 'Poland',
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    });

    // Состояние для показа/скрытия паролей
    const [showCurrentPassword, setShowCurrentPassword] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    // Состояние для отображения модального окна выбора страны на iOS
    const [showCountryPicker, setShowCountryPicker] = useState(false);

    const handleChange = (field, value) => {
        setFormData({
            ...formData,
            [field]: value
        });
    };

    const handleSave = async () => {
        // Валидация данных перед отправкой
        if (formData.newPassword && formData.currentPassword === '') {
            alert('To change the password, enter the current password');
            return;
        }

        if (formData.newPassword && formData.newPassword !== formData.confirmPassword) {
            alert('New password and confirmation do not match');
            return;
        }

        if (!formData.location) {
            alert('Please choose the country');
            return;
        }

        // Здесь будет логика сохранения данных на сервер
        console.log("Saving data:", formData);

        // Имитация запроса
        setTimeout(() => {
            router.goBack();
        }, 500);
    };

    return (
        <ScrollView className="flex-1 bg-white pb-[100px]">
            <SafeAreaView>
                {/* Кастомный заголовок с кнопками назад и сохранить */}
                <View className="flex-row items-center px-4 pt-4 pb-3 bg-white border-b border-gray-100">
                    <TouchableOpacity
                        onPress={() => router.goBack()}
                        className="flex-row items-center"
                    >
                        <ChevronLeft size={20} color="#3f8efc" />
                        <Text className="text-[#3f8efc] ml-1">Go back</Text>
                    </TouchableOpacity>

                    <Text className="text-lg font-semibold ml-10 text-gray-900">Edit profile</Text>
                </View>

                {/* Форма редактирования личных данных */}
                <View className="px-5 pt-6">
                    <Text className="text-gray-900 font-semibold mb-4">Personal information</Text>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">First Name</Text>
                        <TextInput
                            className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900"
                            value={formData.firstName}
                            onChangeText={(text) => handleChange('firstName', text)}
                            placeholder="Enter your firstname"
                        />
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Last Name</Text>
                        <TextInput
                            className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900"
                            value={formData.lastName}
                            onChangeText={(text) => handleChange('lastName', text)}
                            placeholder="Введите фамилию"
                        />
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Email</Text>
                        <TextInput
                            className="bg-gray-50 border border-gray-200 rounded-xl px-4 py-3.5 text-gray-500"
                            value="vladimir.marianciuc@gmail.com"
                            editable={false}
                        />
                        <Text className="text-xs text-gray-500 mt-1">You can't change email</Text>
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Phone Number</Text>
                        <TextInput
                            className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900"
                            value={formData.phone}
                            onChangeText={(text) => handleChange('phone', text)}
                            keyboardType="phone-pad"
                            placeholder="Введите номер телефона"
                        />
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Country</Text>

                        {Platform.OS === 'ios' ? (
                            <>
                                <TouchableOpacity
                                    className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 flex-row justify-between items-center"
                                    onPress={() => setShowCountryPicker(true)}
                                >
                                    <Text className={`${formData.location ? 'text-gray-900' : 'text-gray-400'}`}>
                                        {formData.location || 'Choose the country'}
                                    </Text>
                                    <ChevronDown size={18} color="#9ca3af" />
                                </TouchableOpacity>

                                {showCountryPicker && (
                                    <View className="absolute bottom-0 left-0 right-0 bg-white z-10 border-t border-gray-200">
                                        <View className="flex-row justify-between px-4 py-2 border-b border-gray-200">
                                            <TouchableOpacity onPress={() => setShowCountryPicker(false)}>
                                                <Text className="text-gray-500">Cancel</Text>
                                            </TouchableOpacity>
                                            <TouchableOpacity onPress={() => setShowCountryPicker(false)}>
                                                <Text className="text-[#3f8efc]">Apply</Text>
                                            </TouchableOpacity>
                                        </View>
                                        <Picker
                                            selectedValue={formData.location}
                                            onValueChange={(itemValue) => {
                                                handleChange('location', itemValue);
                                            }}
                                        >
                                            {countries.map((country, index) => (
                                                <Picker.Item key={index} label={country.label} value={country.value} />
                                            ))}
                                        </Picker>
                                    </View>
                                )}
                            </>
                        ) : (
                            <View className="bg-white border border-gray-200 rounded-xl overflow-hidden">
                                <Picker
                                    selectedValue={formData.location}
                                    onValueChange={(itemValue) => handleChange('location', itemValue)}
                                    style={{ height: 50, width: '100%' }}
                                >
                                    {countries.map((country, index) => (
                                        <Picker.Item key={index} label={country.label} value={country.value} />
                                    ))}
                                </Picker>
                            </View>
                        )}
                    </View>
                </View>

                {/* Форма изменения пароля */}
                <View className="px-5 pt-4">
                    <Text className="text-gray-900 font-semibold mb-4">Change password</Text>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Current password</Text>
                        <View className="relative">
                            <TextInput
                                className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900 pr-12"
                                value={formData.currentPassword}
                                onChangeText={(text) => handleChange('currentPassword', text)}
                                secureTextEntry={!showCurrentPassword}
                                placeholder="Enter your current password"
                            />
                            <TouchableOpacity
                                onPress={() => setShowCurrentPassword(!showCurrentPassword)}
                                className="absolute right-3 top-3.5"
                            >
                                {showCurrentPassword ? (
                                    <EyeOff size={20} color="#9ca3af" />
                                ) : (
                                    <Eye size={20} color="#9ca3af" />
                                )}
                            </TouchableOpacity>
                        </View>
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">New Password</Text>
                        <View className="relative">
                            <TextInput
                                className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900 pr-12"
                                value={formData.newPassword}
                                onChangeText={(text) => handleChange('newPassword', text)}
                                secureTextEntry={!showNewPassword}
                                placeholder="Enter a new password"
                            />
                            <TouchableOpacity
                                onPress={() => setShowNewPassword(!showNewPassword)}
                                className="absolute right-3 top-3.5"
                            >
                                {showNewPassword ? (
                                    <EyeOff size={20} color="#9ca3af" />
                                ) : (
                                    <Eye size={20} color="#9ca3af" />
                                )}
                            </TouchableOpacity>
                        </View>
                    </View>

                    <View className="mb-5">
                        <Text className="text-sm text-gray-500 mb-2">Confirm password</Text>
                        <View className="relative">
                            <TextInput
                                className="bg-white border border-gray-200 rounded-xl px-4 py-3.5 text-gray-900 pr-12"
                                value={formData.confirmPassword}
                                onChangeText={(text) => handleChange('confirmPassword', text)}
                                secureTextEntry={!showConfirmPassword}
                                placeholder="Repeat the new password"
                            />
                            <TouchableOpacity
                                onPress={() => setShowConfirmPassword(!showConfirmPassword)}
                                className="absolute right-3 top-3.5"
                            >
                                {showConfirmPassword ? (
                                    <EyeOff size={20} color="#9ca3af" />
                                ) : (
                                    <Eye size={20} color="#9ca3af" />
                                )}
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>

                {/* Кнопка сохранения внизу */}
                <View className="px-5 mt-6 mb-[100px]">
                    <TouchableOpacity
                        className="bg-[#3f8efc] rounded-xl py-4 items-center"
                        onPress={handleSave}
                    >
                        <Text className="text-white font-semibold">Save changes</Text>
                    </TouchableOpacity>
                </View>
            </SafeAreaView>
        </ScrollView>
    )
}

export default EditProfile