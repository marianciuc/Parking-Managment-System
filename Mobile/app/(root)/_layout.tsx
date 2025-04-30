import {Stack} from "expo-router";
import {Gesture, GestureHandlerRootView} from "react-native-gesture-handler";
import BottomMenu from "@/app/components/BottomMenu";
import {NavigationContainer} from "@react-navigation/native";

export default function RootLayout() {
    return (
        <GestureHandlerRootView style={{flex: 1}}>
            <Stack screenOptions={{headerShown: false}}>
                <Stack.Screen name="explore"/>
                <Stack.Screen name="profile "/>
                <Stack.Screen name="family-share "/>
                <Stack.Screen name="edit-profile "/>
                <Stack.Screen name="map-search "/>
                <Stack.Screen name="notifications "/>
                <Stack.Screen name="parking-sessions"/>
                <Stack.Screen name="owned-cars"/>
                <Stack.Screen name="geo-permissions"/>
                <Stack.Screen name="topping-up-first"/>
                <Stack.Screen name="topping-up-summary"/>
                <Stack.Screen name="additional-registration-info"/>
                <Stack.Screen name="payments"/>
                <Stack.Screen name="topping-up-fail"/>
                <Stack.Screen name="topping-up-success"/>
                <Stack.Screen name="parking-session-active"/>
                <Stack.Screen name="add-car"/>
                <Stack.Screen name="car-details"/>
                <Stack.Screen name="otp-verification"/>
            </Stack>
            <BottomMenu />
        </GestureHandlerRootView>

    );
}
