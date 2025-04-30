import React, {useEffect, useMemo, useRef, useState} from 'react';
import { View, StyleSheet } from 'react-native';
import { Images, ShapeSource, SymbolLayer, CircleLayer } from "@rnmapbox/maps";
import { featureCollection, point } from "@turf/helpers";
import { OnPressEvent } from "@rnmapbox/maps/src/types/OnPressEvent";
import { useParking } from "@/app/providers/ParkingProvider";
import icons from "@/constants/icons";
import api from '@/app/http/api'; // Убедитесь, что путь к API правильный

// Используем имеющуюся иконку
const logoIcon = icons.logo;


interface IOpeningHours {
    dayOfWeek: DayOfWeek;
    openingTime?: [number, number];
    closingTime?: [number, number];
    isClosed?: boolean;
}
interface ICapacity {
    capacity: number;
    occupiedSpaces: number;
    placesForDisabled: number;
    occupiedPlacesForDisabled: number;
    placesForElectricCars: number;
    occupiedPlacesForElectricCar: number;
}

interface IAddress {
    id: string | undefined | null;
    countryCode: string;
    city: string;
    postalCode: string;
    street: string;
    buildingNumber: string;
    latitude: number ;
    longitude: number;
    isBelongToAnyInstitution: boolean;
    institutionName: string | undefined | null;
    recordStatus: string | undefined | null;
    creationDate: string | undefined | null;
    modificationDate: string | undefined | null;
}
enum DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

interface IParkingTag{
    id: string | undefined | null;
    name: string | undefined | null;
    description: string | undefined | null;
}

interface IParking {
    id: string;
    name: string;
    imageUrl: string;
    address: IAddress;
    capacity:number;
    occupiedSlots:number;
    is24h : boolean;
    tags : IParkingTag[];
    workingTimeList?: IOpeningHours[];
}


export enum ParkingStatus {
    CREATION_PROCESS = "CREATION_PROCESS",
    TEMPORARY_CLOSED = "TEMPORARY_CLOSED",
    MODERATE = "ON_MODERATION",
    FULL = "FULL",
    CLOSED = "CLOSED",
    OPEN = "OPEN"
}

// interface IParking {
//     id: number;
//     long: number;
//     lat: number;
//     name: string;
//     spots: number;
//     price: string;
//     category: string;
// }

interface ParkingMarkersProps {
    mapRef: React.RefObject<any>;
    cameraRef: React.RefObject<any>;
}

export default function ParkingMarkers({ mapRef, cameraRef }: ParkingMarkersProps) {
    const { selectedParking, setSelectedParking } = useParking<IParking>(null);
    const [parkingList, setParkingList] = useState<IParking[]>([] as IParking[]);

    useEffect(() => {
        const fetchData = async () => {
            try{
                console.log("Fetch parking fetch" , "53.4525613, 14.5383154")

                const response = await api.get(`/api/v1/parking/search`, {
                    params: {
                        page: 0,
                    }
                });

                console.log("REsponse parking fetch" ,response)

                if(response.status < 210){
                    setParkingList(response.data.content)
                }
            } catch (error) {
                console.error("Failed to fetch parking:", error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (selectedParking) {
            // console.log("Перемещение камеры к:", [selectedParking.long, selectedParking.lat]);

            cameraRef.current.setCamera({
                centerCoordinate: [selectedParking.address.longitude, selectedParking.address.latitude - 0.002],
                zoomLevel: 15,
                animationDuration: 1000,
            });
        }
    }, [selectedParking]);

    // Создаем коллекцию фич
    const parkingCollection = useMemo(() => {
        const parkingFeatures = parkingList.map((parking) =>
            point([parking.address.longitude, parking.address.latitude], {
                parking,
                isSelected: selectedParking?.id === parking.id,
                category: "none"
            })
        );
        return featureCollection(parkingFeatures);
    }, [selectedParking, parkingList]);

    // Обработчик нажатия
    const onPointPress = (event: OnPressEvent) => {
        const feature = event.features[0];
        if (feature?.properties?.parking) {
            const parking = feature.properties.parking;
            const isCurrentlySelected = selectedParking?.id === parking.id;

            // Устанавливаем выбранную парковку
            setSelectedParking(isCurrentlySelected ? null : parking);
        }
    };

    // Функция для определения цвета маркера в зависимости от категории
    const getCategoryColor = (category) => {
        switch(category) {
            case 'public': return '#4A90E2'; // Синий
            case 'private': return '#50E3C2'; // Бирюзовый
            case 'commercial': return '#F5A623'; // Оранжевый
            case 'free': return '#7ED321'; // Зеленый
            case 'event': return '#D0021B'; // Красный
            case 'transport': return '#9013FE'; // Фиолетовый
            default: return '#4A90E2'; // По умолчанию синий
        }
    };

    return (
        <>
            {/* Подложка для всех маркеров */}
            <ShapeSource id="parking-backgrounds" shape={parkingCollection}>
                <CircleLayer
                    id="parking-circle-bg"
                    style={{
                        circleColor: ['get', 'category', ['get', 'parking']],
                        circleColorTransition: { duration: 300, delay: 0 },
                        circleRadius: 14,
                        circleStrokeWidth: 2,
                        circleStrokeColor: '#FFFFFF',
                    }}
                    filter={['!', ['get', 'isSelected']]}
                    belowLayerID="parking-selected-circle-bg"
                />
                <CircleLayer
                    id="parking-selected-circle-bg"
                    style={{
                        circleColor: '#ffffff',
                        circleRadius: 18,
                        circleStrokeWidth: 3,
                        circleStrokeColor: ['case',
                            ['has', 'category', ['get', 'parking']],
                            ['get', 'category', ['get', 'parking']],
                            '#4A90E2'
                        ],
                        circleStrokeOpacity: 0.6,
                    }}
                    filter={['==', ['get', 'isSelected'], true]}
                />
            </ShapeSource>

            {/* Маркеры парковок */}
            <ShapeSource
                id="parking-points"
                shape={parkingCollection}
                onPress={onPointPress}
            >
                <SymbolLayer
                    id="parking-symbols"
                    style={{
                        iconImage: logoIcon,
                        iconSize: ['case',
                            ['get', 'isSelected'],
                            0.8,
                            0.6
                        ],
                        iconAllowOverlap: true,
                    }}
                />
            </ShapeSource>
        </>
    );
}

const styles = StyleSheet.create({
    dot: {
        width: 30,
        height: 30,
        borderRadius: 15,
        backgroundColor: 'blue',
        borderWidth: 2,
        borderColor: 'white',
        justifyContent: 'center',
        alignItems: 'center',
    },
    selectedDot: {
        width: 36,
        height: 36,
        borderRadius: 18,
        backgroundColor: 'white',
        borderWidth: 3,
        borderColor: 'blue',
        justifyContent: 'center',
        alignItems: 'center',
    },
    callout: {
        padding: 10,
        borderRadius: 5,
        width: 200,
        backgroundColor: 'white',
    },
    title: {
        fontWeight: 'bold',
        marginBottom: 5,
    },
    details: {
        fontSize: 12,
    },
});