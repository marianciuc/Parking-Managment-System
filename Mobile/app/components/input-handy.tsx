import React, { useState, useRef } from 'react';
import { View, Text, TextInput, Animated, TouchableOpacity } from 'react-native';
import {Eye, EyeClosed, EyeOff} from 'lucide-react-native';

interface InputProps {
    label: string;
    placeholder: string;
    value: string;
    onChange: (text: string) => void;
    type: 'text' | 'password' | 'email' | 'phone';
    passwordicon?: boolean;
}

const InputField: React.FC<InputProps> = ({ label, placeholder, value, onChange, type, passwordicon }) => {
    const [inputValue, setInputValue] = useState(value);
    const [borderColor] = useState(new Animated.Value(0));
    const [isValid, setIsValid] = useState(false);
    const [isPasswordVisible, setIsPasswordVisible] = useState(type !== 'password');
    const iconOpacity = useRef(new Animated.Value(1)).current;

    // Анимация изменения цвета границы
    React.useEffect(() => {
        Animated.timing(borderColor, {
            toValue: inputValue.length > 1 ? 1 : 0,
            duration: 300,
            useNativeDriver: false,
        }).start();

        if (type === 'email') {
            setIsValid(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/.test(inputValue));
        } else if (type === 'phone') {
            setIsValid(/^\d{10}$/.test(inputValue));
        } else {
            setIsValid(inputValue.length > 1);
        }
    }, [inputValue, type]);

    const togglePasswordVisibility = () => {
        Animated.sequence([
            Animated.timing(iconOpacity, {
                toValue: 0,
                duration: 50,
                useNativeDriver: true,
            }),
            Animated.timing(iconOpacity, {
                toValue: 1,
                duration: 50,
                useNativeDriver: true,
            }),
        ]).start();
        setIsPasswordVisible((prev) => !prev);
    };

    const animatedBorderColor = borderColor.interpolate({
        inputRange: [0, 1],
        outputRange: ['#d1d5db', '#34d399'],
    });

    const animatedBackgroundColor = borderColor.interpolate({
        inputRange: [0, 1],
        outputRange: ['#ffffff', '#d1fae5'],
    });

    return (
        <View className="mb-4">
            <View className="flex-col">
                <Text className="text-black text-sm font-poppins leading-snug">{label}</Text>
                <Animated.View
                    style={{
                        width: '100%',
                        height: 52,
                        borderRadius: 8,
                        flexDirection: 'row',
                        alignItems: 'center',
                        marginTop: 6,
                        borderWidth: 1,
                        borderColor: animatedBorderColor,
                        backgroundColor: animatedBackgroundColor,
                        paddingHorizontal: 12,
                    }}
                >
                    <TextInput
                        className="flex-1 h-full text-black font-poppins"
                        placeholder={placeholder}
                        placeholderTextColor="#7b7b7b"
                        secureTextEntry={!isPasswordVisible && type === 'password'}
                        keyboardType={type === 'email' ? 'email-address' : type === 'phone' ? 'phone-pad' : 'default'}
                        value={inputValue}
                        onChangeText={(text) => {
                            setInputValue(text);
                            onChange(text);
                        }}
                    />
                    {passwordicon && type === 'password' && (
                        <TouchableOpacity
                            onPress={togglePasswordVisibility}
                            className="pr-4"
                            activeOpacity={0.7}
                        >
                            <Animated.View style={{ opacity: iconOpacity }}>
                                {isPasswordVisible ? (
                                    <EyeClosed size={24} color="#343434" />
                                ) : (
                                    <Eye size={24} color="#343434" strokeWidth={2} />
                                )}
                            </Animated.View>
                        </TouchableOpacity>
                    )}
                </Animated.View>
            </View>
        </View>
    );
};

export default InputField;
