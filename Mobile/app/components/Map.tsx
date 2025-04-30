import React, {useRef} from 'react';
import { View, Dimensions } from 'react-native';
import MapboxGL from "@rnmapbox/maps";
import ParkingMarkers from "@/app/components/ParkingMarkers";

const accessToken = 'pk.eyJ1IjoieW9za2lraWdhaSIsImEiOiJjbTdwMHltamwwZGloMmlxdjR3bmUydzFvIn0.jg1Wrl260sUHhgnhpdMzxg';

// Инициализация вне компонента
MapboxGL.setAccessToken(accessToken);

export default function Map() {
    const mapRef = useRef(null);
    const cameraRef = useRef(null);


    return (
        <View style={{ flex: 1 }}>
            <MapboxGL.MapView
                ref={mapRef}
                style={{ flex: 1 }}
                styleURL={MapboxGL.StyleURL.Street}
                logoEnabled={false}
            >
                <MapboxGL.Camera
                    ref={cameraRef}
                    zoomLevel={14}
                    centerCoordinate={[53.4525613, 14.5383154]}
                />
                <ParkingMarkers mapRef={mapRef} cameraRef={cameraRef}/>
            </MapboxGL.MapView>
        </View>
    );
}