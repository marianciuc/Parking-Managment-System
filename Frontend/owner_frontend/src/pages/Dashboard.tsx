import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";
import DashboardCards from "@/components/blocks/DashboardCards.tsx";
import DashboardManagement from "@/components/blocks/DashboardManagement.tsx";
import DashboardRecentSessions from "@/components/blocks/DashboardRecentSessions.tsx";
import RevenuePlot from "@/components/plots/RevenuePlot.tsx";
import AverageParkingSessionsByWeekDay from "@/components/plots/AverageParkingSessionsByWeekDay.tsx";
import UseOfSubscriptionsPlot from "@/components/plots/UseOfSubscriptionsPlot.tsx";
import SessionsActivity from "@/components/plots/SessionsActivity.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";
import {useLocation, useNavigate, useParams} from "react-router-dom";
import { motion } from "framer-motion";
import {OccupancyPlot} from "@/components/plots/OccupancyPlot.tsx";



const tabs = ["analytics", "control", "sessions"];



function Dashboard() {


    const {parkingId} = useParams();

    const navigate = useNavigate();



    const location = useLocation();

    const activeTab = tabs.find((tab) => location.pathname.includes(tab)) || "analytics";

    console.log(activeTab);

    const handleTabChange = (value: string) => {
        if (parkingId) {
            navigate(`/parking/${parkingId}/dashboard/${value}`);
        } else {
            console.error("Parking ID is missing.");
        }
    };





    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            {/*<div className="flex justify-between items-center mb-6">*/}
            {/*    <h1 className="text-3xl font-bold"></h1>*/}
            {/*</div>*/}



            {/* Основные метрики */}
            <PageHeader title={"Parking Dashboard"} subtitle={"Manage, Monitor, and Optimize Your Parking Operations in Real-Time"}>
                <DashboardCards/>
            </PageHeader>

            {/* Информация о подписке */}



            {/*<Separator className="mb-6"/>*/}

            {/* Вкладки */}
            <Tabs defaultValue={activeTab} onValueChange={handleTabChange} >
                <motion.div className="image-background flex flex-col p-6 items-center justify-center shadow rounded-lg mb-6 "
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.3 }}>
                <TabsList className="">
                    <TabsTrigger value="analytics">Analytics</TabsTrigger>
                    <TabsTrigger value="control">Management</TabsTrigger>
                    <TabsTrigger value="sessions">Recent sessions</TabsTrigger>
                </TabsList>
                </motion.div>

                <TabsContent value="analytics">
                    <motion.div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6"
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.3 }}>
                        <RevenuePlot/>
                        <OccupancyPlot/>

                    </motion.div>

                    <motion.div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6"
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.3 }}>
                        <UseOfSubscriptionsPlot/>
                        <AverageParkingSessionsByWeekDay/>

                    </motion.div>

                    <motion.div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6"
                                initial={{ opacity: 0, y: 10 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.3 }}>
                        <SessionsActivity/>

                        {/*<SessionsActivity/>*/}
                    </motion.div>
                </TabsContent>


                {/* Вкладка: Управление */}
                <TabsContent value="control">
                    <DashboardManagement/>
                </TabsContent>


                {/* Вкладка: Последние сессии */}
                <TabsContent value="sessions">
                    <DashboardRecentSessions/>
                </TabsContent>

            </Tabs>
        </div>
    );
}

export default Dashboard;