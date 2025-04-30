import React from "react";

const formatTimestampFromArray = (timestampArray?: Array<number>): string => {
    // Convert Proxy Array to a regular array
    if(!timestampArray) return "Invalid Date";
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
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
    }).format(date);
};

const PrettyDate: React.FC<{ timestamp?: Array<number>}> = ({ timestamp }) => {
    return <span>{formatTimestampFromArray(timestamp)}</span>;
};


export default PrettyDate;