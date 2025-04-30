import {useContext, useEffect, useState} from "react";
import {ArrowDownRight} from "lucide-react";
import {IAvgSessionsData, ISessionMetrics, ITopVehiclesInPeakHours} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {useParams} from "react-router-dom";
import {Context} from "@/main.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";
import DataNotAvailable from "../ui/data-not-available";
import { motion } from "framer-motion";

function SessionMetrics() {

    const {parkingId} = useParams<{ parkingId: string }>();


    const [loading, setLoading] = useState<boolean>(false);

    const {sessionStore} = useContext(Context);


    const [metrics, setMetrics] = useState<ISessionMetrics>({
        activeSessions: 23,
        averageTimeOfActiveSessions: 23.5,
        totalSessionsForWeek: 91,
        totalSessionsForMonth: 70,
        totalSessionsForYear: 100,
        totalSessionsForAllTime: 0,
        personalTariffProposition: [],
        // @ts-ignore
        premiumTariffProposition: null,
        peakHours: [],
        topVehiclesInPeakHours: []
    });
    //
    const [firstBlockCar] = useState<IAvgSessionsData>({
        vehicleId: "",
        plateNumber: "BBK2906",
        avgSessionsPerMonth: 7
    });
    // const [firstBlockCar] = useState<IAvgSessionsData>(metrics.personalTariffProposition[0] ?? undefined);

    const [secondBlockCar] = useState<ITopVehiclesInPeakHours>({
        vehicleId: "",
        plateNumber: "KA2356BK",
        sessionCount: 5
    });
    // const [secondBlockCar] = useState<ITopVehiclesInPeakHours>(metrics.topVehiclesInPeakHours[0]);


    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching sessions for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await sessionStore.fetchMetrics(parkingId);


                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }

                if (sessionStore.metrics) {
                    setMetrics(sessionStore.metrics);
                }


            } catch (error) {
                console.error("Failed to fetch sessions metrics:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, sessionStore]);


    useEffect(() => {
        console.log(metrics);
    }, [metrics]);


    return (
        <>

            {/* Карточки */}
            <motion.div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 w-full max-w-6xl"
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}>
                {/* Карточка 1 */}
                <div className="bg-white shadow-md rounded-xl p-6 flex flex-col justify-center border relative">
                    {loading ? <SmallLoader className={"min-h-[200px]"}/>
                        : firstBlockCar ?
                            <div className="flex flex-col justify-between h-full">
                                <div>
                                    <p className="text-lg text-gray-800 font-semibold">
                                        <span className="font-bold">{firstBlockCar.plateNumber}</span> has an
                                        average of <span
                                        className="font-bold">{firstBlockCar.avgSessionsPerMonth}</span> sessions a
                                        month.
                                    </p>
                                    <p className="text-sm text-gray-500 mt-3">
                                        Do you want to assign a personal tariff for this vehicle?
                                    </p>
                                </div>
                                <button
                                    className="mt-5 flex  items-center justify-center px-4 py-3 rounded-lg bg-gray-900 hover:bg-gray-700 text-white font-medium text-sm focus:outline-none focus:ring-2 focus:ring-gray-600">
                                    Assignment <ArrowDownRight className="ml-2 w-4 h-4"/>
                                </button>
                            </div>
                            : <DataNotAvailable/>}
                </div>

                {/* Карточка 2 */}
                <div className="bg-white shadow-md rounded-xl p-6 flex flex-col justify-between border">
                    {loading ? <SmallLoader className={"min-h-[200px]"}/>
                        : secondBlockCar ?
                            <div className="flex flex-col justify-between h-full">
                                <div>
                                    <p className="text-lg text-gray-800 font-semibold">
                                        <span className="font-bold">{secondBlockCar.plateNumber}</span> has been
                                        parking during peak hours for the last <span
                                        className="font-bold">{secondBlockCar.sessionCount}</span>{" "}
                                        days.
                                    </p>
                                    <p className="text-sm text-gray-500 mt-3">
                                        Do you want to assign a premium tariff for peak-hour parking?
                                    </p>
                                </div>
                                <button
                                    className="mt-5 flex items-center justify-center px-4 py-3 rounded-lg bg-gray-900 hover:bg-gray-700 text-white font-medium text-sm focus:outline-none focus:ring-2 focus:ring-gray-600">
                                    Assignment <ArrowDownRight className="ml-2 w-4 h-4"/>
                                </button>
                            </div>
                            : <DataNotAvailable/>}

                </div>

                {/* Карточка 3 */}
                <div className="bg-white shadow-md rounded-xl p-6 flex flex-col justify-center border">

                    {loading ? <SmallLoader className={"min-h-[200px]"}/>
                        : metrics.activeSessions ?
                            <div className="flex flex-col justify-between h-full">
                                <div>
                                    <h1 className="text-4xl font-extrabold text-gray-900 tracking-tight">
                                        {metrics.activeSessions}
                                    </h1>
                                    <p className="text-lg font-medium text-gray-600 mt-2">
                                        Active Sessions
                                    </p>
                                    <p className="text-sm text-gray-500 mt-3">
                                        Would you like to generate a weekly report or review payment
                                        statuses?
                                    </p>
                                </div>
                                <button
                                    disabled
                                    className="mt-5 flex items-center justify-center px-4 py-3 rounded-lg bg-gray-200 text-gray-500 font-medium text-sm cursor-not-allowed"
                                >
                                    Soon <ArrowDownRight className="ml-2 w-4 h-4"/>
                                </button>
                            </div>
                            : <DataNotAvailable/>}

                </div>

                {/* Карточка 4 */}
                <div className="bg-white shadow-md rounded-xl p-6 flex flex-col justify-between border">
                    {loading ? <SmallLoader className={"min-h-[200px]"}/>
                        : metrics.totalSessionsForWeek ?
                            <div className="flex flex-col justify-between h-full">
                                <div>
                                    <h1 className="text-4xl font-extrabold text-gray-900 tracking-tight">
                                        {metrics.totalSessionsForWeek}
                                    </h1>
                                    <p className="text-lg font-medium text-gray-600 mt-2">
                                        Closed sessions for last week
                                    </p>
                                    <p className="text-sm text-gray-500 mt-3">
                                        Would you like to generate a weekly report or review payment
                                        statuses?
                                    </p>
                                </div>
                                <button
                                    disabled
                                    className="mt-5 flex items-center justify-center px-4 py-3 rounded-lg bg-gray-200 text-gray-500 font-medium text-sm cursor-not-allowed"
                                >
                                    Soon <ArrowDownRight className="ml-2 w-4 h-4"/>
                                </button>
                            </div>
                            : <DataNotAvailable/>}

                </div>
            </motion.div>


        </>
    );
}

export default SessionMetrics;