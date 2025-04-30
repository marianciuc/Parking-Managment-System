import React from "react";
import {View,Text} from "react-native";

const formatTimestampFromArray = (timestampArray: Array<number>): string => {
    // Convert Proxy Array to a regular array
    const dateArray = Array.from(timestampArray);

    // Ensure the array has the correct number of elements (year, month, day, hour, minute, second)
    if (dateArray.length < 6) return "Invalid Date";

    // Extract components from the array
    const [year, month, day, hour, minute, second] = dateArray;

    // Create a new Date object (month is zero-indexed)
    const date = new Date(year, month - 1, day, hour, minute, second);

    // Handle invalid date case
    if (isNaN(date.getTime())) return "Invalid Date";

    // Format the date to a readable string
    return new Intl.DateTimeFormat("en-US", {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "numeric",
        minute: "numeric",
        hour12: false,
    }).format(date);
};

const PrettyDate: React.FC<{ timestamp: Array<number> }> = ({ timestamp }) => {
    return formatTimestampFromArray(timestamp); // Просто возвращаем строку, не оборачивая в <View> или <Text>
};



export default PrettyDate;