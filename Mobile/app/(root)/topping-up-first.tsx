import {View, Text, SafeAreaView, TextInput, TouchableOpacity} from 'react-native'
import React, {useEffect, useRef, useState} from 'react'
import Header from "@/app/components/header";
import ButtonHandy from "@/app/components/button-handy";
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import {useNavigation} from "@react-navigation/native";

const ToppingUpFirst = () => {

    const navigation = useNavigation();
    const router = useExpoRouter();
    const [value, setValue] = useState('30');
    const inputRef = useRef<TextInput>(null);

    const handleNavigate = () => {
        router.push(`/topping-up-summary?value=${value}`); // Передаем значение через URL
    };


    useEffect(() => {
        setTimeout(() => {
            if (inputRef.current) {
                inputRef.current.focus();
            }
        }, 100);
    }, []);

    const handleButton = () => {
        router.navigate("/topping-up-summary");
    }

    const handlePress = (num: number) => {
        setValue(num.toString());
        if (inputRef.current) {
            inputRef.current.focus();
        }
    };

    return (
        <SafeAreaView className={"w-full h-full bg-white"}>
            <Header header={"Topping Up"} style={"user"} textColor={"#000000"}/>
            <View className={"px-4 flex-col mt-2"}>
                <Text className={"font-poppins text-lg "}>Topping up on account</Text>
                <Text className={"font-poppins-medium text-3xl mb-3"}>bob@gmail.com</Text>
                <Text className={"text-gray-500"}>Account balance: 10.31$</Text>
            </View>
            <View className={"items-center mt-24"}>
                <View className="flex-row items-center w-4/5 border-b-4 border-blue-500">
                    <TextInput
                        ref={inputRef}
                        value={value}
                        onChangeText={(text) => setValue(text.replace(/[^0-9]/g, ''))}
                        keyboardType="numeric"
                        placeholder="Enter amount"
                        placeholderTextColor="gray-200"
                        className="w-full text-black font-poppins-medium text-4xl leading-snug py-2 text-center mt-5"
                    />
                </View>
            </View>

            <View className="flex-row mt-4 space-x-4 items-center justify-center">
                {[10, 30, 50, 100].map((num) => (
                    <TouchableOpacity
                        key={num}
                        onPress={() => handlePress(num)}
                        className="px-2 py-2 bg-white rounded-lg items-center"
                    >
                        <View className={"border border-gray-300 rounded-full px-6 py-1"}>
                            <Text className="font-poppins-medium text-gray-500 text-sm">{num}$</Text>
                        </View>
                    </TouchableOpacity>
                ))}
            </View>
            <View className={"px-4 mt-12"}>
                <ButtonHandy label={"Continue"} style={"primary"} onPress={handleNavigate}/>
            </View>

        </SafeAreaView>
    )
}
export default ToppingUpFirst
