import React, { useState } from 'react';
import { View, Text, Pressable, FlatList } from 'react-native';
import Modal from 'react-native-modal';

const genders = [
    { id: 'male', label: '👨 Male' },
    { id: 'female', label: '👩‍🚀 Female' },
    { id: 'other', label: '🐷 Other' },
];

const GenderDropdown = () => {
    const [selectedGender, setSelectedGender] = useState<string | null>(null);
    const [isModalVisible, setModalVisible] = useState(false);

    return (
        <View className="w-full">
            <Pressable
                onPress={() => setModalVisible(true)}
                className={`w-full h-14 border rounded-md flex-row items-center mt-1.5 p-4 ${
                    selectedGender ? 'border-green-500 bg-green-100' : 'border-gray-300 bg-white'
                }`}
            >
                <Text className={`text-gray-500 font-poppins leading-[21px] ${
                    selectedGender ? 'text-green-700' : ''
                }`}>
                    {selectedGender ? genders.find(g => g.id === selectedGender)?.label : 'Click to select gender'}
                </Text>
            </Pressable>

            {/* Модальное окно с выбором */}
            <Modal isVisible={isModalVisible} onBackdropPress={() => setModalVisible(false)}>
                <View className="bg-white p-4 rounded-lg">
                    <Text className="text-lg font-semibold mb-3">Choose gender</Text>
                    <FlatList
                        data={genders}
                        keyExtractor={(item) => item.id}
                        renderItem={({ item }) => (
                            <Pressable
                                className={`p-3 border-b border-gray-200 ${
                                    selectedGender === item.id ? 'bg-green-100' : ''
                                }`}
                                onPress={() => {
                                    setSelectedGender(item.id);
                                    setModalVisible(false);
                                }}
                            >
                                <Text className={`text-gray-900 ${selectedGender === item.id ? 'text-green-700 font-bold' : ''}`}>
                                    {item.label}
                                </Text>
                            </Pressable>
                        )}
                    />
                </View>
            </Modal>
        </View>
    );
};

export default GenderDropdown;
