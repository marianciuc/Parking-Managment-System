import {View, Text, SafeAreaView, Image, TouchableOpacity, Modal, TouchableWithoutFeedback, Alert} from 'react-native';
import React, {useContext, useEffect, useState} from 'react';
import Header from "@/app/components/header";
import images from "@/constants/images";
import ButtonHandy from "@/app/components/button-handy";
import {useExpoRouter} from "expo-router/build/global-state/router-store";
import {StripeProvider, useStripe} from '@stripe/stripe-react-native';
import {useSearchParams} from "expo-router/build/hooks";
import api from "@/app/http/api";
import { Context } from '../_layout';
import {IAccountBalance, IDriver} from "@/app/(root)/explore";

const ToppingUpSummary = () => {
    const router = useExpoRouter();
    const [modalVisible, setModalVisible] = useState(false);
    const [paymentMethod, setPaymentMethod] = useState('Card payment');
    const [paymentImage, setPaymentImage] = useState(images.visa);

    const [driver, setDriver] = useState<IDriver | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            try{

                const response = await api.get(`/api/v1/drivers`)

                console.log("Response drivers" ,response)

                if(response.status  < 210){
                    setDriver(response.data)
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
        };

        fetchData();
    }, []);

    const [driverBalance, setDriverBalance] = useState<IAccountBalance>({} as IAccountBalance);

    useEffect(() => {
        const fetchData = async () => {
            try{

                const response = await api.get(`/api/v1/accounts-balance/owned/${driver?.userId}`);

                console.log("Response drivers balance" ,response)

                if(response.status  < 210){
                    setDriverBalance(response.data)
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
        };

        fetchData();
    }, [driver?.userId]);

    const [loading, setLoading] = useState(false);
    const { initPaymentSheet, presentPaymentSheet } = useStripe();
    const [paymentSheetInitialized, setPaymentSheetInitialized] = useState(false);

    const searchParams = useSearchParams();
    const value = searchParams.get('value');
    const amount = value ? value : '0';

    const handleChangePaymentMethod = (method: string, image: any) => {
        setPaymentMethod(method);
        setPaymentImage(image);
        setModalVisible(false);
    };

    const [publishableKey, setPublishableKey] = useState('');
    const fetchPublishableKey = async () => {
        try {
            const response = await api.get('/api/v1/stripe/public-key');
            const key = response.data;
            setPublishableKey(key);
            return key;
        } catch (error) {
            console.error('Error fetching publishable key:', error);
            Alert.alert('Ошибка', 'Не удалось получить ключ Stripe. Пожалуйста, попробуйте позже.');
            return null;
        }
    };

    useEffect(() => {
        const initializeStripe = async () => {
            const key = await fetchPublishableKey();
            if (key) {
                await initializePaymentSheet();
            }
        };

        initializeStripe();
    }, []);

    // Инициализация payment sheet
    const initializePaymentSheet = async () => {
        try {
            setLoading(true);

            // Запрос на сервер для получения payment intent
            const response = await api.post(`/api/v1/balance/operations/accounts/${driverBalance.owenerId}/deposit`, {
                amount: Number(amount) * 100, // конвертируем в центы для Stripe
            });

            const { paymentIntentClientSecret, ephemeralKey, customerId } = response.data;

            // Инициализация payment sheet
            const { error } = await initPaymentSheet({
                merchantDisplayName: "Your Company Name",
                customerId: customerId,
                customerEphemeralKeySecret: ephemeralKey,
                paymentIntentClientSecret: paymentIntentClientSecret,
                // Настройки внешнего вида
                appearance: {
                    colors: {
                        primary: '#007AFF', // основной цвет (можно заменить своим)
                    },
                },
                defaultBillingDetails: {
                    email: driver?.email, // можно подставить email пользователя
                }
            });

            if (error) {
                Alert.alert('Ошибка', `Не удалось инициализировать платежный лист: ${error.message}`);
                setPaymentSheetInitialized(false);
            } else {
                // Устанавливаем флаг, что Payment Sheet инициализирован
                setPaymentSheetInitialized(true);
            }

            setLoading(false);
        } catch (error) {
            console.error('Error creating payment intent:', error);
            setLoading(false);
            setPaymentSheetInitialized(false);
            Alert.alert('Ошибка', 'Не удалось создать платеж. Пожалуйста, попробуйте позже.');
        }
    };

    // Функция для открытия payment sheet
    const openPaymentSheet = async () => {
        if (loading) return;

        // Проверяем, инициализирован ли Payment Sheet
        if (!paymentSheetInitialized) {
            Alert.alert('Подождите', 'Платежная форма еще инициализируется. Пожалуйста, подождите или попробуйте позже.');
            return;
        }

        try {
            const { error } = await presentPaymentSheet();

            if (error) {
                console.error('Payment failed:', error);
                Alert.alert('Ошибка платежа', error.message);
            } else {
                // Платеж прошел успешно
                console.log('Payment successful!');
                Alert.alert('Успешно', 'Оплата прошла успешно!', [
                    {
                        text: 'OK',
                        onPress: () => router.push('/dashboard') // Перенаправление после успешной оплаты
                    }
                ]);
            }
        } catch (error) {
            console.error('Error presenting payment sheet:', error);
            Alert.alert('Ошибка', 'Не удалось открыть платежный лист. Пожалуйста, попробуйте позже.');
        }
    };

    // Обработчик нажатия на кнопку оплаты
    const handlePaymentPress = async () => {
        if (!paymentSheetInitialized) {
            // Если Payment Sheet еще не инициализирован, сначала инициализируем его
            await initializePaymentSheet();
        }
        // Затем открываем Payment Sheet
        await openPaymentSheet();
    };

    return (
        <StripeProvider
            publishableKey={publishableKey || process.env.EXPO_PUBLIC_STRIPE_PUBLISHABLE_KEY!}
        >
            <SafeAreaView className="w-full h-full bg-white">
                <Header header={"Topping Up"} style={"user"} textColor={"black"}/>

                <View
                    className="w-full mt-3 py-3 px-5 bg-white flex-col rounded-3xl shadow-[0_25px_14px_rgba(0,0,0,0.04)]">
                    <Text className="text-lg font-poppins-medium">You're topping up</Text>
                    <Text className="text-6xl font-poppins-medium leading-snug mt-2">{amount}$</Text>
                    <Text className="text-sm font-poppins-light text-gray-500 mt-5">For account</Text>
                    <Text className="mt-0.5 mb-5 font-poppins">bob@gmail.com</Text>
                </View>

                <View className="w-full mt-8 justify-between flex-row items-center py-4 px-6 mb-4">
                    <View className="flex-row items-center">
                        <Image source={paymentImage} style={{width: 40, height: undefined, aspectRatio: 1}}
                               resizeMode="contain"/>
                        <Text className="ml-4 font-poppins-medium">{paymentMethod}</Text>
                    </View>
                    <TouchableOpacity onPress={() => setModalVisible(true)}>
                        <Text className="text-blue-500 font-poppins-medium">Change</Text>
                    </TouchableOpacity>
                </View>

                <View className="px-6">
                    <Text className="text-sm text-gray-500">You will receive confirmation of the top-up {"\n"}via email
                        bob@gmail.com</Text>
                    <Text className="text-sm text-gray-500 mt-2">By pressing the 'Next' button, you confirm that you
                        have
                        read and agreed to our Terms and Conditions as well as our Privacy Policy. Please make sure to
                        review them carefully before proceeding. If you do not agree with any part of these terms, you
                        should not continue with the process.</Text>
                </View>

                <View className="flex-1 justify-end px-4 mb-1">
                    <ButtonHandy
                        label={loading ? "Загрузка..." : "Go to payment"}
                        style={"primary"}
                        onPress={handlePaymentPress}
                        disabled={loading}
                    />
                </View>

                <Modal
                    animationType="fade"
                    transparent={true}
                    visible={modalVisible}
                    onRequestClose={() => setModalVisible(false)}
                >
                    <TouchableWithoutFeedback onPress={() => setModalVisible(false)}>
                        <View style={styles.modalBackground}>
                            <View style={styles.modalContent}>
                                <Text className="text-xl font-poppins-medium mb-4">Choose a payment method</Text>

                                <TouchableOpacity className={"flex-row items-center mb-2"}
                                                  onPress={() => handleChangePaymentMethod('Card payment', images.visa)}>
                                    <Image source={images.visa} style={{width: undefined, height: 30, aspectRatio: 1}}
                                           resizeMode="contain"/>
                                    <Text className="ml-2 text-lg font-poppins text-blue-500">Card payment</Text>
                                </TouchableOpacity>

                                <TouchableOpacity className={"flex-row items-center mb-2"}
                                                  onPress={() => handleChangePaymentMethod('PayPal', images.paypal)}>
                                    <Image source={images.paypal} style={{width: undefined, height: 30, aspectRatio: 1}}
                                           resizeMode="contain"/>
                                    <Text className="ml-2 text-lg font-poppins text-blue-500 mt-3">PayPal</Text>
                                </TouchableOpacity>

                                <TouchableOpacity className={"flex-row items-center mb-2"}
                                                  onPress={() => handleChangePaymentMethod('Blik', images.blik)}>
                                    <Image source={images.blik} style={{width: undefined, height: 30, aspectRatio: 1}}
                                           resizeMode="contain"/>
                                    <Text className="ml-2 text-lg font-poppins text-blue-500 mt-3">Blik</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </TouchableWithoutFeedback>
                </Modal>
            </SafeAreaView>
        </StripeProvider>
    );
};

// Стили для модального окна
const styles = {
    modalBackground: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)', // Прозрачный фон
    },
    modalContent: {
        width: '90%',
        backgroundColor: 'white',
        borderRadius: 20,
        padding: 20,
        justifyContent: 'center',
    },
};

export default ToppingUpSummary;