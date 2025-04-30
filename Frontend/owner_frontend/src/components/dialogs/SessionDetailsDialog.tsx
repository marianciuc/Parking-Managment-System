import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {useContext, useEffect, useState , forwardRef} from "react";
import {ESessionStatus, ISessionDetails, statusColors} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {Context} from "@/main.tsx";
import PrettyDate from "@/components/ui/pretty-date.tsx";



const DetailsContent = forwardRef<HTMLDivElement, { open: boolean; sessionId: string }>(
    ({ open, sessionId }, ref) => {
        const [session, setSession] = useState<ISessionDetails>({} as ISessionDetails);
        const { sessionStore } = useContext(Context);

        const [loading, setLoading] = useState(false);

        useEffect(() => {
            if (open) {
                const fetchData = async () => {
                    console.log(sessionId);
                    setLoading(true);
                    try {
                        if (!sessionId) {
                            console.warn("No sessionId passed");
                            return;
                        }
                        const toastMessage: IToastInfo | void = await sessionStore.fetchSessionDetails(sessionId);

                        if (toastMessage) {
                            console.log(toastMessage);
                        }
                        setSession(sessionStore.currentSession);
                    } catch (error) {
                        console.error("Failed to fetch tariffs:", error);
                    }
                    setLoading(false);
                };
                console.log("session changed")
                fetchData();
            }
        }, [open, sessionId, sessionStore]);

        const transactions = [
            { paid: 25, paymentTime: "24.10.23 15:22", timeAdded: "2 hours" },
            // { paid: 25, paymentTime: "24.10.23 15:22", timeAdded: "2 hours" },
            // { paid: 25, paymentTime: "24.10.23 15:22", timeAdded: "2 hours" },
        ];
        const status = session.status || 'N/A';
        const statusClass = statusColors[status as ESessionStatus] || 'text-gray-400';

        useEffect(() => {
            console.log(session);
        }, [session]);

        if(loading){
            return (
                <DialogContent>
                    Loading....
                </DialogContent>
            )
        }

        return (
            <DialogContent className="w-[700px]" ref={ref}>
                <DialogHeader>
                    <DialogTitle>Session Details</DialogTitle>
                    <DialogDescription>
                        <span>Id:</span> <span>{session.id}</span>
                    </DialogDescription>
                </DialogHeader>

                <div className="space-y-4">
                    <div className="border rounded-md p-4 flex flex-col gap-2">
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Status:</span>{" "}
                            <span className={`${statusClass} font-semibold`}>
                                {status}
                            </span>
                        </p>
                        <p className={"flex flex-row justify-between"}>
                        <span className="font-medium">Session started at:</span>{" "}
                            <span>{sessionStore.sessionsPage.content.find(value => value.id == sessionId)?.startTime
                                ? <PrettyDate timestamp={sessionStore.sessionsPage.content.find(value => value.id ==sessionId)?.startTime}/>
                                : "N/A"}
                            </span>
                        </p>
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Session ended at:</span>
                            <span>
                            {sessionStore.sessionsPage.content.find(value => value.id == sessionId)?.endTime
                                //@ts-ignore
                                ? <PrettyDate timestamp={sessionStore.sessionsPage.content.find(value => value.id ==sessionId)?.endTime}/>
                                : "N/A"}
                        </span>
                        </p>
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Total time paid:</span> <span>{session.totalPaid} {session.currency}</span>
                        </p>
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Total amount:</span> <span>{session.totalAmount} {session.currency}</span>
                        </p>
                    </div>

                    <div className="border rounded-md p-4">
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Vehicle owner:</span> <span>{ "N/A"}</span>

                        </p>
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Vehicle plate number:</span> <span>{session.vehiclePlateNumber}</span>
                        </p>
                        <p className="flex flex-row justify-between">
                            <span className="font-medium">Access list:</span> <span>{session.vehicleAccessList}</span>
                        </p>
                    </div>

                    <div className="border rounded-md p-4">
                        <p className="font-medium">Transactions:</p>
                        <table className="w-full text-left border-collapse border border-gray-200">
                            <thead>
                            <tr>
                                <th className="border border-gray-200 px-2 py-1">Paid</th>
                                <th className="border border-gray-200 px-2 py-1">Payment time</th>
                                <th className="border border-gray-200 px-2 py-1">Time added</th>
                            </tr>
                            </thead>
                            <tbody>
                            {transactions.map((transaction, index) => (
                                <tr key={index}>
                                    <td className="border border-gray-200 px-2 py-1">{transaction.paid} PLN</td>
                                    <td className="border border-gray-200 px-2 py-1">{transaction.paymentTime}</td>
                                    <td className="border border-gray-200 px-2 py-1">{transaction.timeAdded}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="flex flex-row justify-between" >
                    <Button variant="destructive" onClick={()=> alert("Not implemented")}>Stop session</Button>
                    <Button variant="default" onClick={()=> alert("Not implemented")}>Cancel</Button>
                </div>
            </DialogContent>
        );
    }
);

function SessionDetailsDialog({ sessionId }: { sessionId: string }) {

    useEffect(()=>{
        console.log("Session Details");
        console.log(sessionId);
    },[])

    const [open, setOpen] = useState(false);

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <div className="text-sm px-2 py-1.5 cursor-pointer">View Details</div>
            </DialogTrigger>
            <DetailsContent open={open} sessionId={sessionId} />
        </Dialog>
    );
}

export default SessionDetailsDialog;
