import React, {useEffect, useState} from 'react';
import {View, Text, TextInput, Button, SafeAreaView} from 'react-native';
import VehicleService from '@/app/services/VehicleService'
import ButtonHandy from "@/app/components/button-handy";
import InputHandy from "@/app/components/input-handy";
import Header from "@/app/components/header";
import {AxiosError} from "axios";

const AddCar = () => {
    const [plateNumber, setPlateNumber] = useState('');
    const [brand, setBrand] = useState('');
    const [model, setModel] = useState('');

    const handleAddCar = async () => {
        console.log('Plate Number:', plateNumber);
        console.log('Brand:', brand);
        console.log('Model:', model);

        try {
            const response = await VehicleService.createVehicle(plateNumber, brand, model, true);
            console.log('Vehicle added:', response.data);
        } catch (e: unknown) {
            const error = e as AxiosError;
            console.log("Error adding vehicle:", error.response?.data);
        }
    };



    return (

            <SafeAreaView className="w-full h-full bg-white">
                <Header header={""} style={"user"} textColor={"black"}/>
                <View className={"px-4 flex-col mb-4"}>
                    <Text className={"text-3xl font-poppins-semibold"}>Adding Your car</Text>
                    <Text className={"w-82 font-poppins text-gray-500 mt-2"}>Enter your car model, license plate, and other essential information to ensure a hassle-free parking experience</Text>
                </View>
                <View className="p-4 flex-col justify-between">
                    <View className="">
                        <InputHandy value={plateNumber} placeholder={"Enter plate number"} onChange={setPlateNumber} type={"text"} label={"Enter plate number"} />
                        <InputHandy value={brand} placeholder={"ex. Aubi"} onChange={setBrand} type={"text"} label={"Enter car brand"} />
                        <InputHandy value={model} placeholder={"ex. RS12"} onChange={setModel} type={"text"} label={"Enter car model"} />
                    </View>

                    <View className={""}>
                        <ButtonHandy onPress={handleAddCar} label={'Add car'} style={"primary"}/>
                    </View>

                </View>
            </SafeAreaView>

    );
};

export default AddCar;
