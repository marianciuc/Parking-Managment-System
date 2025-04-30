import {View, Text, SafeAreaView, Image} from 'react-native'
import React from 'react'
import Header from "@/app/components/header";
import ButtonHandy from "@/app/components/button-handy";
import images from "@/constants/images";

const ToppingUpSuccess = () => {

    const handleToExplore = () => {

    }

    const handleTryAgain = () => {

    }

    return (
        <SafeAreaView className={"w-full h-full bg-white"}>
            <Header header="Topping Up" style={"user"} textColor={"black"}/>
            <View className="flex-1 flex-col justify-between">
                <View className="items-center mt-20">
                    <Image
                        source={images.fail}
                        resizeMode="cover"
                        className="w-72 h-72 rounded-full"
                    />
                    <Text className="text-center font-poppins-semibold text-2xl mt-10">
                        Success! The funds will be added to Your account
                    </Text>
                </View>
                <View className="justify-end px-4">
                    <ButtonHandy label={"Back to Home"} style={"primary"} onPress={handleToExplore}/>
                </View>

            </View>
        </SafeAreaView>
    )
}
export default ToppingUpSuccess
