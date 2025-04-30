import IParking from "../models/common/IParking.ts";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import "./styles/parkingSettings.css"
import ParkingSettingsGeneralInformation from "@/components/tabs/ParkingSettingsGeneralInformation.tsx";
import ParkingSettingDetails from "@/components/tabs/ParkingSettingDetails.tsx";
import ParkingSettingsWorkingHours from "@/components/tabs/ParkingSettingsWorkingHours.tsx";
import { useLocation, useNavigate} from "react-router-dom";
import {useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";
import DashboardSubscriptionsInformation from "@/components/blocks/DashboardSubscriptionsInformation.tsx";


const tabs = ["general", "details", "working-hours", "subscription"];


function ParkingSettings() {

    const {parkingId} = useParams();

    const navigate = useNavigate();
    const location = useLocation();

    const [parking, setParking] = useState<IParking | null>(null);

    const {parkingStore} = useContext(Context);

    useEffect(() => {
        let isMounted = true;
        if (parkingId) {
            parkingStore.fetchParking(parkingId).then(
                (parking) => {
                    if (isMounted) {
                        setParking(parking);
                        console.log(parking);
                    }
                }
            ).catch((e) => {
                console.error("Failed to fetch parking:", e);
            });
        }
        return () => {
            isMounted = false;
        };
    }, [parkingId, parkingStore]);

    const activeTab = tabs.find((tab) => location.pathname.includes(tab)) || "general";

    console.log(activeTab);

    const handleTabChange = (value: string) => {
        if (parkingId) {
            navigate(`/parking/${parkingId}/settings/${value}`);
        } else {
            console.error("Parking ID is missing.");
        }
    };

    return <div
        className="flex items-center justify-start gap-10 h-full  flex-col  m-8">
        {parking &&
            <Tabs defaultValue={activeTab} onValueChange={handleTabChange} className="w-full">
                <PageHeader title={`${parking?.name} Settings`} subtitle="Welcome to the Parking Settings block - your all-in-one toolkit for managing and customizing every detail
                of your parking system. Control operational hours, and available spaces with ease. Adjust tags,
                facilities, and features to make your parking spots stand out." >
                <TabsList>
                    <TabsTrigger value="general">General Information</TabsTrigger>
                    <TabsTrigger value="details">Details</TabsTrigger>
                    <TabsTrigger value="working-hours">Working Hours</TabsTrigger>
                    <TabsTrigger value="subscription">Subscription</TabsTrigger>
                </TabsList>
                </PageHeader>
                <div className="mt-6 bg-white rounded-lg p-10">
                <TabsContent value="general">
                    <ParkingSettingsGeneralInformation parking={parking} />
                </TabsContent>
                <TabsContent value="details">
                    <ParkingSettingDetails parking={parking} />
                </TabsContent>
                <TabsContent value="working-hours">
                    <ParkingSettingsWorkingHours parking={parking} />
                </TabsContent>
                    <TabsContent value="subscription">
                        <DashboardSubscriptionsInformation parking={parking}/>
                    </TabsContent>
                </div>
            </Tabs>
        }


    </div>
}

export default ParkingSettings;