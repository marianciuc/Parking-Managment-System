import React from "react";
import {View, Text, TouchableOpacity, Image} from "react-native";
import { useNavigation } from "@react-navigation/native";
import {ArrowLeft} from "lucide-react-native";
import images from "@/constants/images"; // Импорт useNavigation
 // Например, иконка стрелки
interface HeaderProps {
    header:string;
    style: "auth" | "header" | "user" | "map";
    textColor?: string;
}
// Исправляем тип пропсов: ожидаем объект с полем header типа string
const Header: React.FC<HeaderProps> = ({header, style, textColor = "#ffffff"}) => {
    const navigation = useNavigation();

    const handleArrowClick = () => {
        navigation.goBack();
    };

    if (style === "auth") {
        return (
            <View className="flex-row w-full items-center justify-between mt-6 mb-8 px-4">
                <TouchableOpacity onPress={handleArrowClick}>
                    <ArrowLeft className={"size-24"} color="#000000" />
                </TouchableOpacity>
                <Image source={images.logoBlue} className="w-7 h-8 relative overflow-hidden" />
            </View>
        )

    } else if (style === "header") {
        return(
            <View className="flex-row w-full mt-5 mb-4 pb-4 rounded-lg px-2 rounded-xl items-center ">
                <TouchableOpacity onPress={handleArrowClick}>
                    <ArrowLeft className={"size-24"} color="#000" />
                </TouchableOpacity>
                <Text className="text-2xl ml-4 font-bold text-gray-800">{header}</Text>
            </View>
        )
    } else if (style === "user") {

        return(
            <View className="flex-row w-full items-center justify-between mt-6 mb-8 px-4">
                <TouchableOpacity onPress={handleArrowClick}>
                    <ArrowLeft className={"size-24"} color={textColor} />
                </TouchableOpacity>
                <Text className={"text-2xl font-poppins-semibold text-white"} style={{ color: textColor }}>{header}</Text>
                <View className="w-7 h-8 relative overflow-hidden" />
            </View>
            )

    } else if (style == "map"){
        return(
            <View className="flex-row w-full items-center justify-between mt-6 mb-8 px-4 bg-blue-500">
                <TouchableOpacity onPress={handleArrowClick}>
                    <ArrowLeft className={"size-24"} color={textColor} />
                </TouchableOpacity>
                <Text className={"text-2xl font-poppins-semibold text-white"} style={{ color: textColor }}>{header}</Text>
                <View className="w-7 h-8 relative overflow-hidden" />
            </View>
        )
    }

};

export default Header;