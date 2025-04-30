"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { Marker } from "@react-google-maps/api";
import { Loader } from "@googlemaps/js-api-loader";

const loader = new Loader({
    apiKey: import.meta.env.VITE_REACT_APP_GOOGLE_MAPS_API_KEY as string,
    version: "weekly",
});
//@ts-ignore
const LocationMap = ({ location, coordinates, setCoorditnates , markerIsDraggable }) => {
    const mapRef = useRef(null);
    const [map, setMap] = useState(null);
    const [markerPosition, setMarkerPosition] = useState(null);

    useEffect(() => {
        //@ts-ignore
        let mapInstance;

        loader
            .load()
            .then(() => {
                //@ts-ignore
                mapInstance = new google.maps.Map(mapRef.current, {
                    center: { lat: 0, lng: 0 },
                    zoom: 2,
                });
                //@ts-ignore
                setMap(mapInstance);

                if(coordinates.lat !== null) setMarkerPosition(coordinates);

                //@ts-ignore
                mapInstance.addListener("click", (event) => {
                    if(!markerIsDraggable) return;
                    const lat = event.latLng.lat();
                    const lng = event.latLng.lng();
                    //@ts-ignore
                    setMarkerPosition({ lat, lng });
                    setCoorditnates({ lat, lng });
                })
            })
            .catch((err) => {
                console.error("Error loading Google Maps API:", err);
            });

        return () => {
            //@ts-ignore
            if (mapInstance) {
                google.maps.event.clearInstanceListeners(mapInstance);
            }
        };
    }, [setCoorditnates]);

    useEffect(() => {
        if (map && location.lat && location.lng) {
            const { lat, lng, zoom = 8 } = location;

            // Smooth zoom transition
            //@ts-ignore
            const currentZoom = map.getZoom();
            const zoomSteps = Math.abs(zoom - currentZoom);
            const duration = 500; // Total duration in ms
            const stepTime = duration / zoomSteps;

            let step = 0;
            const smoothZoom = () => {
                if (step < zoomSteps) {
                    const newZoom = zoom > currentZoom
                        ? currentZoom + step
                        : currentZoom - step;
                    //@ts-ignore
                    map.setZoom(newZoom);
                    step++;
                    setTimeout(smoothZoom, stepTime);
                } else {
                    //@ts-ignore
                    map.setZoom(zoom); // Ensure the final zoom level
                }
            };
            smoothZoom();

            // Smooth center transition
            //@ts-ignore
            map.panTo({ lat, lng });
        }
    }, [location, map]);

    const handleMarkerDragEnd = useCallback(
        //@ts-ignore
        (event) => {
            const lat = event.latLng.lat();
            const lng = event.latLng.lng();
            //@ts-ignore
            setMarkerPosition({ lat, lng });
            setCoorditnates({ lat, lng });
        },
        [setCoorditnates]
    );



    return (
        <div style={{ width: "100%", height: "400px", position: "relative" }}>
            <div ref={mapRef} style={{ width: "100%", height: "100%" }}>
                {map && markerPosition && (
                    <Marker
                        position={markerPosition}
                        //@ts-ignore
                        map={map}
                        draggable={markerIsDraggable}
                        onDragEnd={handleMarkerDragEnd}
                    />
                )}
            </div>
        </div>
    );
};

export default LocationMap;

