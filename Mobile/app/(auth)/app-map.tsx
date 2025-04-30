import {View, Text} from 'react-native'
import React from 'react'
import {Link} from "expo-router";

const AppMap = () => {
    return (
        <View className={"container mt-5 items-center justify-center"}>
            <Text className="font-bold text-lg my-10 text-3xl"> Welcome to Restate </Text>

            <Link href="/(root)/explore"> explore </Link>
            <Link href="/(root)/map-search"> map-search </Link>
            <Link href="/(root)/parking-sessions"> parking sessions </Link>
            <Link href="/(root)/profile"> profile </Link>
            <Link href="/(root)/properties/"> Property </Link>
            <Link href="/(auth)/welcome"> Welcome </Link>
        </View>
    )
}
export default AppMap
