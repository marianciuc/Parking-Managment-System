import React, { PropsWithChildren, createContext, useContext, useState } from 'react';

interface ParkingContextType {
    selectedParking: any; // Можно заменить на более конкретный тип, если необходимо
    setSelectedParking: React.Dispatch<React.SetStateAction<any>>;
}

const ParkingContext = createContext<ParkingContextType>({
    selectedParking: null,
    setSelectedParking: () => {},
});

export default function ParkingProvider({ children }: PropsWithChildren) {
    const [selectedParking, setSelectedParking] = useState<any>(null);

    return (
        <ParkingContext.Provider value={{ selectedParking, setSelectedParking }}>
            {children}
        </ParkingContext.Provider>
    );
}

export const useParking = () => useContext(ParkingContext);
