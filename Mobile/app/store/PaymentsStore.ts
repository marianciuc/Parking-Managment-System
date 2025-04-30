import { makeAutoObservable } from "mobx";
import api from "@/app/http/api";
import handleError from "@/app/utils/ErrorHandler";

export default class PaymentStore {
    stripeClientKey: string | null = null;
    clientSecret: string | null = null;
    isLoading: boolean = false;

    constructor() {
        makeAutoObservable(this);
    }

    setIsLoading(isLoading: boolean) {
        this.isLoading = isLoading;
    }

    setStripeClientKey(key: string | null) {
        this.stripeClientKey = key;
    }

    getStripeClientKey = async (): Promise<boolean> => {
        this.setIsLoading(true);
        try {
            const response = await api.get('/api/v1/stripe');
            this.setStripeClientKey(response.data);
            console.log("Stripe client key:", response.data);
            return true;
        } catch (e: unknown) {
            handleError(e, "Receiving error Stripe client key");
            return false;
        } finally {
            this.setIsLoading(false);
        }
    };

    setClientSecret(clientSecret: string | null) {
        this.clientSecret = clientSecret;
    }

    createPaymentIntent = async (amount: number, currency: string): Promise<boolean> => {
        this.setIsLoading(true);
        try {
            const response = await api.post("/api/v1/create-payment-intent", { amount, currency });
            this.setClientSecret(response.data.clientSecret);
            console.log("PaymentIntent clientSecret:", response.data.clientSecret);
            return true;
        } catch (e: unknown) {
            handleError(e, "Ошибка создания PaymentIntent");
            return false;
        } finally {
            this.setIsLoading(false);
        }
    };

}
