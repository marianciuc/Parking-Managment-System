import React, { useState } from "react";
import { Button, View, Text, Alert } from "react-native";
import { initializePaymentSheet, openPaymentSheet } from "@/app/services/StripeService";
import UserStore from "@/app/store/UserStore"; // Импортируй свои сервисы

const StripeTestPage = () => {

    const userStore = new UserStore();

    const userId = userStore.getUserId();




    const [isPaymentReady, setIsPaymentReady] = useState(false);

    const setupPayment = async () => {
        console.log("Payment user id" + userId);
        console.log("env key" + process.env.EXPO_PUBLIC_STRIPE_PUBLISHABLE_KEY);
        const success = await initializePaymentSheet({ accountId: userId, amount: 500 });
        setIsPaymentReady(success);
    };


    const handlePayment = async () => {

        const success = await openPaymentSheet();
        if (success) {
            Alert.alert("✅ Payment successful!");
        } else {
            Alert.alert("❌ Payment failed.");
        }
    };

    return (
        <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
            <Text style={{ marginBottom: 20 }}>Stripe Payment Test</Text>

            {/* Кнопка для настройки PaymentSheet */}
            <Button title="Setup Payment" onPress={setupPayment} />

            {/* Кнопка для начала платежа */}
            <Button
                title="Pay Now"
                onPress={handlePayment}
                disabled={!isPaymentReady}
                style={{ marginTop: 20 }}
            />
        </View>
    );
};

export default StripeTestPage;
