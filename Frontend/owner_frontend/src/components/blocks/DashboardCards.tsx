import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Car, DollarSign, PieChart, TrendingUp} from "lucide-react";
import { useState} from "react";
import SmallLoader from "@/components/ui/small-loader.tsx";
import DataNotAvailable from "@/components/ui/data-not-available.tsx";

import { motion } from "framer-motion";

interface ICurrentOccupation {
    occupied:number;
    total:number;
}

interface IRevenue{
    total:number;
    daily:number;
}

interface IBill{
    perClient:number;
    numberOfClients:number;
}
function DashboardCards(){

    //
    // const {parkingId} = useParams<{ parkingId: string }>();
    //
    //
    // const {sessionStore} = useContext(Context);


    const [currentOccupation] = useState<ICurrentOccupation>({occupied:50, total:60});

    const [loading] = useState(false);

    const [revenue] = useState<IRevenue>({total:4680, daily: 156.02});

    const [bill] = useState<IBill>({perClient:20.02,numberOfClients:20});



    // useEffect(() => {
    //     const fetchData = async () => {
    //         setLoading(true);
    //         try {
    //             if (!parkingId) {
    //                 console.warn("No parkingId passed");
    //                 return;
    //             }
    //
    //             console.log("Fetching reviews for parkingId:", parkingId);
    //
    //             const toastMessage: IToastInfo | void = await sessionStore.fetchMetrics(parkingId);
    //
    //
    //
    //             if (toastMessage) {
    //                 console.log("Toast Message:", toastMessage);
    //             }
    //
    //
    //
    //             if (sessionStore.metrics) {
    //
    //                 // set(reviewStore.reviewsPage as IPage<IReview>);
    //             }
    //
    //         } catch (error) {
    //             console.error("Failed to fetch reviews:", error);
    //         }
    //         setLoading(false);
    //     };
    //
    //     fetchData();
    // }, [parkingId, page, pageSize, reviewStore]);
    //
    //



    return (
        <motion.div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-12"
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3 }}>
            <Card className="bg-gradient-to-br from-white/80 to-gray-200 shadow-xl rounded-2xl border border-gray-300/40 backdrop-blur-lg p-6">
            {loading ? <SmallLoader className="min-h-36"/>
            : currentOccupation ? <><CardHeader className="flex flex-col items-center text-center">
                    <PieChart className="w-8 h-8 text-gray-700" />
                    <CardTitle className="mt-2 text-gray-900 text-lg font-semibold">Occupancy</CardTitle>
                </CardHeader>
                <CardContent className="text-center">
                    <p className="text-5xl font-semibold text-gray-900">{ currentOccupation.total >0 && (100*currentOccupation.occupied/currentOccupation.total).toFixed(2)}%</p>
                    <p className="text-base text-gray-600">Current occupancy</p>
                    <p className="mt-2 text-sm text-gray-500">{currentOccupation.occupied} out of {currentOccupation.total} slots are occupied</p>
                </CardContent></>
             : <DataNotAvailable/>}
            </Card>

            <Card className="bg-gradient-to-br from-white/80 to-gray-200 shadow-xl rounded-2xl border border-gray-300/40 backdrop-blur-lg p-6">
            {loading ? <SmallLoader className="min-h-36"/>
                : revenue? <> <CardHeader className="flex flex-col items-center text-center">
                        <TrendingUp className="w-8 h-8 text-gray-700" />
                        <CardTitle className="mt-2 text-gray-900 text-lg font-semibold">Revenue for the month</CardTitle>
                    </CardHeader>
                    <CardContent className="text-center">
                        <p className="text-5xl font-semibold text-gray-900">{revenue.total} PLN</p>
                        <p className="text-base text-gray-600">Total revenue</p>
                        <p className="mt-2 text-sm text-gray-500">Average daily revenue: {revenue.daily} PLN</p>
                    </CardContent></>
                : <DataNotAvailable/>}
            </Card>

            <Card className="bg-gradient-to-br from-white/80 to-gray-200 shadow-xl rounded-2xl border border-gray-300/40 backdrop-blur-lg p-6">
            {loading ? <SmallLoader className="min-h-36"/>
                : currentOccupation?<> <CardHeader className="flex flex-col items-center text-center">
                        <Car className="w-8 h-8 text-gray-700" />
                        <CardTitle className="mt-2 text-gray-900 text-lg font-semibold">Free slots</CardTitle>
                    </CardHeader>
                    <CardContent className="text-center">
                        <p className="text-5xl font-semibold text-gray-900">{currentOccupation.total - currentOccupation.occupied}</p>
                        <p className="text-base text-gray-600">Available</p>
                        <p className="mt-2 text-sm text-gray-500">Total capacity: {currentOccupation.total}</p>
                    </CardContent></>
                 : <DataNotAvailable/>}
            </Card>

            <Card className="bg-gradient-to-br from-white/80 to-gray-200 shadow-xl rounded-2xl border border-gray-300/40 backdrop-blur-lg p-6">
            {loading ? <SmallLoader className="min-h-36"/>
                : bill? <><CardHeader className="flex flex-col items-center text-center">
                        <DollarSign className="w-8 h-8 text-gray-700" />
                        <CardTitle className="mt-2 text-gray-900 text-lg font-semibold">Average bill</CardTitle>
                    </CardHeader>
                    <CardContent className="text-center">
                        <p className="text-5xl font-semibold text-gray-900">{bill.perClient} PLN</p>
                        <p className="text-base text-gray-600">Per client</p>
                        <p className="mt-2 text-sm text-gray-500">Total clients for the month: {bill.numberOfClients}</p>
                    </CardContent></>
               : <DataNotAvailable/>}
            </Card>
        </motion.div>
    )
}

export default DashboardCards;