import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {ArrowLeft, ArrowRight, Settings, ShieldCheck, XCircle} from "lucide-react";
import {Button} from "@/components/ui/button";
import {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Context} from "@/main.tsx";
import {EGateStatus, EGateType, IGate} from "@/models/common/IGate.ts";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {Switch} from "@/components/ui/switch";
import { motion } from "framer-motion";
import {Badge} from "@/components/ui/badge";
import SmallLoader from "@/components/ui/small-loader.tsx";


function GateControl({ gateData } :{gateData:IGate}) {

    const [gate, setGate] = useState<IGate>(gateData);

    const {parkingId} = useParams<{ parkingId: string }>();

    const {gatesManagementStore} = useContext(Context);



    const openGate = async () => {
        if (parkingId) {
            try {
                await gatesManagementStore.openGate(parkingId, gate.id);
                const newGate = gatesManagementStore.gates.find((newGate) => newGate.id === gate.id);
                if (newGate !== undefined) {
                    setGate(newGate);
                }
            } catch (error) {
                console.error("Failed to open gate:", error);
            }
        }
    };

    const closeGate = async () => {
        if(parkingId){
            try{
                await gatesManagementStore.closeGate(parkingId, gate.id);
                const newGate = gatesManagementStore.gates.find((newGate) => newGate.id === gate.id);
                if (newGate !== undefined) {
                    setGate(newGate);
                }
            }catch(error){
                console.log(error);
            }
        }
    }



    const switchManualMode = async (value: boolean) => {
        if(parkingId){
            try{
                await gatesManagementStore.changeManualMode(parkingId, gate.id,value);
                const newGate = gatesManagementStore.gates.find((newGate) => newGate.id === gate.id);
                if (newGate !== undefined) {
                    setGate(newGate);
                }
            }catch(error){
                console.log(error);
            }
        }
    };

    const toggleGate = async () => {
        if(parkingId){
            if(gate.status===EGateStatus.OPEN){
                await closeGate();
            }else{
               await openGate();
            }
        }
    };

    return (
        // <Card className="shadow-lg rounded-lg border border-gray-200 p-4 w-full">
        //     <CardHeader>
        //         <CardTitle className="flex items-center space-x-2 text-xl font-bold text-gray-900">
        //             <ShieldCheck className="w-6 h-6 text-gray-700" />
        //             <span>{gate.name}</span>
        //         </CardTitle>
        //     </CardHeader>
        //     <CardContent className="text-center">
        //         <p className="text-lg font-medium text-gray-700">
        //             Status: <span className={gate.status === EGateStatus.OPEN ? "text-green-500" : "text-red-500"}>{gate.status}</span>
        //         </p>
        //         <p className="mt-3 text-sm text-gray-500">
        //             {gate.isManualMode ? "Manual control enabled." : "Automatic mode enabled." }
        //         </p>
        //         <div className="mt-4 flex justify-center items-center space-x-2">
        //             <Settings className="w-4 h-4 text-gray-700" />
        //             <span>Automatic Mode</span>
        //             <Switch checked={gate.isManualMode} onCheckedChange={switchManualMode} />
        //         </div>
        //         {gate.isManualMode && (
        //             <Button
        //                 className={`mt-4 px-5 py-2.5 rounded-lg ${
        //                     gate.status === EGateStatus.OPEN ? "bg-red-500 hover:bg-red-600" : "bg-green-500 hover:bg-green-600"
        //                 } text-white shadow-md transition-all duration-200`}
        //                 onClick={toggleGate}
        //             >
        //                 {gate.status === EGateStatus.OPEN ? <><XCircle className="w-4 h-4 mr-2" /> Close</> : <><ShieldCheck className="w-4 h-4 mr-2" /> Open</>}
        //             </Button>
        //         )}
        //     </CardContent>
        // </Card>
        <motion.div
            key={gate.id}
            className="w-full"
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
        >
            <Card className="w-full shadow-lg rounded-2xl border border-gray-300 p-5 bg-white">
                <CardHeader>
                    <CardTitle className="flex items-center space-x-3 text-xl font-semibold text-gray-900">
                        {gate.type === EGateType.IN ? (
                            <div className="flex flex-row"><ArrowRight className="w-6 h-6 text-gray-700" />
                            <ShieldCheck className="w-7 h-7 text-gray-600" /></div>
                        ) : (
                            <div className="flex flex-row"><ShieldCheck className="w-7 h-7 text-gray-600" />
                            <ArrowLeft className="w-6 h-6 text-gray-700" /></div>
                        )}
                        <span>{gate.name}</span>
                        <Badge
                            className={`ml-auto px-3 py-1 rounded-lg text-sm font-medium ${
                                gate.status === "OPEN" ? "bg-green-100 text-green-600" : "bg-red-100 text-red-600"
                            }`}
                        >
                            {gate.status}
                        </Badge>
                    </CardTitle>
                </CardHeader>
                <CardContent className="text-center space-y-4 flex flex-row justify-between">
                    <div className="space-y-4 text-left p-4 ">
                        <div className="flex justify-center items-center space-x-3 text-left">
                            <Settings className="w-4 h-4 text-gray-700" />
                            <span>Manual Mode</span>
                            <Switch checked={gate.isManualMode} onCheckedChange={switchManualMode} />
                        </div>
                        <p className="text-base text-gray-600">
                            {gate.isManualMode ? "Manual mode enabled" : "Automatic mode enabled"}
                        </p>
                    </div>
                        <Button
                            className={`mt-3 px-6 py-2 rounded-xl transition-all duration-200 shadow-md flex items-center gap-2 ${
                                gate.status === "OPEN"
                                    ? "bg-red-500 hover:bg-red-600"
                                    : "bg-green-500 hover:bg-green-600"
                            } text-white`}
                            onClick={toggleGate}
                            disabled={!gate.isManualMode}
                        >
                            {gate.status === "OPEN" ? (
                                <>
                                    <XCircle className="w-4 h-4" />
                                    Close Gate
                                </>
                            ) : (
                                <>
                                    <ShieldCheck className="w-4 h-4" />
                                    Open Gate
                                </>
                            )}
                        </Button>
                </CardContent>
            </Card>
        </motion.div>
    );
}

export default function DashboardManagement() {

    //
    const {parkingId} = useParams<{ parkingId: string }>();


    const {parkingStore, gatesManagementStore} = useContext(Context);

    const [gates, setGates] = useState<IGate[]>([] as IGate[]);
    const [loading, setLoading] = useState<boolean>(true);



    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching access list for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await gatesManagementStore.fetchGatesData(parkingId);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }



                if (gatesManagementStore.gates) {
                    setGates(gatesManagementStore.gates);
                }

            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
            setLoading(false);
        };

         fetchData();
    }, [parkingId, parkingStore,gatesManagementStore]);

    return (
        <div className="flex flex-col justify-center items-start gap-6 p-6">
            <p className="mt-3 text-sm text-gray-500 self-center">
            Use this function to control the status of the barrier. Make sure that there are no obstacles in the vicinity
            before opening or closing the barrier.
            </p>
            {loading ? <SmallLoader /> :
                gates.map((gate) => (
                <GateControl key={gate.id} gateData={gate} />
            ))}
        </div>
    );
}