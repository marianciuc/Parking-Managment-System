import {Text, TouchableOpacity} from 'react-native'
import React from 'react'

interface ButtonProps {
    label: string,
    onPress: () => void,
    style: "primary" | "secondary" | "tertiary" | "primary-white",
    color?: string,

}


const ButtonHandy: React.FC<ButtonProps> = ({label,onPress,style,}) => {
    if (style === "primary") {
        return (
            <TouchableOpacity
                className="bg-[#438eff] rounded-xl w-full py-4 mt-4 h-14 items-center justify-center"
                onPress={onPress}
            >
                <Text className="text-white text-base font-poppins-semibold">{label}</Text>
            </TouchableOpacity>
        )
    } else if (style === "primary-white") {
        return (
            <TouchableOpacity
                className="bg-white rounded-3xl w-full py-4 mt-4 h-14 items-center justify-center"
                onPress={onPress}
            >
                <Text className="text-blue-500 font-poppins-semibold">{label}</Text>
            </TouchableOpacity>
        )
    }else if (style === "secondary") {
        return (
            <TouchableOpacity
                className="bg-white border border-[#438eff] rounded-xl w-full py-4 mt-4 h-14 items-center justify-center"
                onPress={onPress}
            >
                <Text className="text-[#438eff] text-base leading-snug font-poppins-semibold">{label}</Text>
            </TouchableOpacity>
        )
    }
}
export default ButtonHandy
