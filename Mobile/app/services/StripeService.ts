import { initPaymentSheet, presentPaymentSheet } from "@stripe/stripe-react-native";
import { fetchStripePublicKey, createPaymentIntent } from "@/app/services/PaymentService";

export const initializePaymentSheet = async ({ accountId, amount }: { accountId: string; amount: number }) => {

    try {
        const publishableKey = await fetchStripePublicKey();
        if (!publishableKey) throw new Error("Stripe public key is missing");

        const paymentIntent = await createPaymentIntent({ accountId, amount });
        if (!paymentIntent || !paymentIntent.clientSecret) throw new Error("Failed to create PaymentIntent");

        const { error } = await initPaymentSheet({
            merchantDisplayName: "HandyParking",
            paymentIntentClientSecret: paymentIntent.clientSecret,
            allowsDelayedPaymentMethods: true,
        });

        if (error) {
            console.error("Error initializing PaymentSheet:", error);
            return false;
        }

        return true;
    } catch (error) {
        console.error("Error setting up PaymentSheet:", error);
        return false;
    }
};

export const openPaymentSheet = async () => {
    try {
        const { error } = await presentPaymentSheet();
        if (error) {
            console.error("Error opening PaymentSheet:", error);
            return false;
        }

        console.log("Payment successful!");
        return true;
    } catch (error) {
        console.error("Error during payment:", error);
        return false;
    }
};
