import api from "@/app/http/api";
import { AxiosResponse } from "axios";

const use_mock = process.env.EXPO_PUBLIC_USE_MOCK;

export const fetchStripePublicKey = async () => {
    if (use_mock) {
        return process.env.EXPO_PUBLIC_STRIPE_PUBLISHABLE_KEY;
    }

    try {
        const response = await api.get("/api/v1/stripe/public-key");

        if (response.status !== 200 || !response.data) {
            throw new Error(`Error fetching key: ${response.status}`);
        }

        console.log("Stripe Public Key:", response.data);
        return response.data;
    } catch (error) {
        console.error("error to getting public key Stripe:", error);
        return null;
    }
};

export const createPaymentIntent = async ({ accountId, amount }: { accountId: string; amount: number }) => {
    try {
        const response = await api.post(`/api/v1/balance/operations/accounts/${accountId}/deposit`, {
            amount,
            currency: "USD",
        });

        if (response.status !== 200) {
            throw new Error(`Error creating PaymentIntent: ${response.status}`);
        }

        return response.data;
    } catch (error) {
        console.error("Error creating PaymentIntent:", error);
        return null;
    }
};





export const sendDepositRequest = async ({ accountId, amount }: { accountId: string; amount: number }): Promise<AxiosResponse> => {
    return api.post(`/api/v1/balance/operations/accounts/${accountId}/deposit`, { amount, currency: "USD" });
};

