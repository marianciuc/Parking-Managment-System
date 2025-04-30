import {View, Text, TouchableOpacity} from 'react-native'
import React from 'react'
import Header from "@/app/components/header";
import {Check, EyeOff} from "lucide-react-native";
import ButtonHandy from "@/app/components/button-handy";
import {useExpoRouter} from "expo-router/build/global-state/router-store";

const TransactionComponent = () => {

    const [transactionDetails, setTransactionDetails] = React.useState({
        parkingName: "Galaxy Centrum parking",
        dateTime: "19 October, 12:04",
        amount: "-44.80$",

    })


    return (
        <View className="w-full flex-row justify-between items-center overflow-hidden mb-4">
            <View className="flex-row items-center flex-wrap flex-1">
                <View className="bg-green-300 rounded-lg p-2">
                    <Check color={"#0EAA00"} />
                </View>
                <View className="ml-2 max-w-[75%]"> {/* Ограничение ширины */}
                    <Text className="font-poppins text-lg flex-shrink">Galaxy Centrum parking</Text>
                    <Text className="text-gray-500 font-poppins flex-shrink">19 October, 12:04</Text>
                </View>
            </View>
            <View className="flex-shrink-0">
                <Text className="text-xl font-poppins-medium">-44.80$</Text>
            </View>
        </View>

    )


}

const Payments = () => {

    const router = useExpoRouter();

    const handleTopUp = () => {
        router.navigate("/topping-up-success")
    }

    return (
        <View className={"w-full h-full bg-[#f2f2f2]"}>
            <View className="w-full h-[350px] bg-[#3f8efc] absolute top-0"/>
            <View className={"mt-14"}/>
            <Header header="Payments" style={"user"} textColor={"white"}/>
            <View className={"px-4"}>
                <View className={"flex-row items-center justify-between mb-16"}>
                    <View className={"flex-col"}>
                        <Text className={"text-white font-poppins-light text-lg"}>Your balance</Text>
                        <Text className={"text-white font-poppins-semibold text-5xl leading-snug"}>1333.12$</Text>
                    </View>
                    <TouchableOpacity>
                        <EyeOff color={"#FFF"} size={32}/>
                    </TouchableOpacity>
                </View>
                <ButtonHandy style={"primary-white"} onPress={handleTopUp} label={"Top up"}/>
            </View>

            <View className={"mt-14 w-full px-4"}>
                <View className={"bg-white rounded-3xl px-4 py-3 "}>
                    <View className={"flex-row justify-between items-center mb-8"}>
                        <Text className={"text-xl font-poppins-medium"}>Transactions</Text>
                        <TouchableOpacity>
                            <Text className={"text-blue-500 font-poppins-medium"}>See all</Text>
                        </TouchableOpacity>
                    </View>
                    <TransactionComponent/>
                    <TransactionComponent/>
                    <TransactionComponent/>
                    <TransactionComponent/>
                </View>

            </View>


        </View>
    )
}
export default Payments
