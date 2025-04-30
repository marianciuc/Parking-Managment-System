import {View, Text, SafeAreaView, Image} from 'react-native'
import React from 'react'
import Header from "@/app/components/header";
import ButtonHandy from "@/app/components/button-handy";
import images from "@/constants/images";

const ToppingUpFail = () => {
    const handleToExplore = () => {

    }

    const handleTryAgain = () => {

    }

    return (
        <SafeAreaView className="w-full flex-1 bg-white">
            <Header header="Topping Up" style="user" textColor="#000"/>
            <View className="flex-1 flex-col justify-between">
                <View className="items-center mt-20">
                    <Image
                        source={images.fail}
                        resizeMode="cover"
                        className="w-72 h-72 rounded-full"
                    />
                    <Text className="text-center font-poppins-semibold text-2xl mt-10">
                        Oops! Something went wrong
                    </Text>
                </View>
                <View className="px-4 flex-1 justify-end">
                    <ButtonHandy label="Try Again" style="primary" onPress={handleTryAgain}/>
                    <ButtonHandy label="Back to explore" style="secondary" onPress={handleToExplore}/>
                </View>
            </View>
        </SafeAreaView>

    )
}
export default ToppingUpFail
