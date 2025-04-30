import {
    View,
    Text,
    TouchableWithoutFeedback,
    Keyboard
} from 'react-native';
import React, { useState } from 'react';
import Header from "@/app/components/header";
import GenderDropdown from "@/app/components/dropdown-handy";
import InputField from "@/app/components/input-handy";
import ButtonHandy from "@/app/components/button-handy";
import { useExpoRouter } from "expo-router/build/global-state/router-store";

const AdditionalRegistrationInfo = () => {
    const router = useExpoRouter();
    const [gender, setGender] = useState<string | null>();
    const [nickname, setNickname] = useState('');
    const [number, setNumber] = useState('');

    const buttonNavigate = () => {
        router.navigate("(root)/geo-permission");
    };

    return (
        <TouchableWithoutFeedback onPress={() => Keyboard.dismiss()}>
            <View className="w-full h-full">
                <Header header={"Additional information"} style={"auth"} />
                <View className="mx-4 mt-5">
                    <Text className="text-3xl font-semibold leading-tight mb-12">
                        Additional information
                    </Text>

                    <View>
                        <InputField
                            label="Name"
                            placeholder="Enter your name (optional)"
                            value={nickname}
                            onChange={(text: string) => setNickname(text)} // Используйте правильную функцию
                            type="text"
                        />
                    </View>

                    <View>
                        <InputField
                            label="Phone number"
                            placeholder="ex. +48(228) 229 333"
                            value={number}
                            onChange={(text: string) => setNumber(text)} // Используйте правильную функцию
                            type="phone"
                        />
                    </View>

                    <View>
                        <Text className={"text-black text-sm font-poppins leading-[21px]"}>Select gender</Text>
                        <GenderDropdown />
                    </View>

                    <ButtonHandy
                        label="Continue"
                        onPress={buttonNavigate}
                        style="secondary"
                    />
                </View>
            </View>
        </TouchableWithoutFeedback>
    );
};

export default AdditionalRegistrationInfo;
