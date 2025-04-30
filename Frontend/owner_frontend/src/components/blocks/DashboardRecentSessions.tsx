import {Card, CardContent, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import { ISessionRecord} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {useParams} from "react-router-dom";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import PrettyDate from "@/components/ui/pretty-date.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";







function DashboardRecentSessions(){

    const {sessionStore} = useContext(Context);

    const {parkingId} = useParams<{ parkingId: string }>();

    const [sessions, setSessions] = useState<ISessionRecord[]>([]);


    const [loading, setLoading] = useState<boolean>(false);



    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching sessions for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await sessionStore.fetchSessionsData(parkingId,0,10);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }


                // Ensure sessionStore.sessions is populated before updating state
                if (sessionStore.sessionsPage) {
                    setSessions(sessionStore.sessionsPage.content);
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, sessionStore]);



    console.log(sessions);
    return (<Card>
        <CardHeader>
            <CardTitle>Recent sessions</CardTitle>
        </CardHeader>
        <CardContent>
            {loading ? <SmallLoader /> :
            <Table>
                {/*<TableCaption>Recent sessions</TableCaption>*/}
                <TableHeader>
                    <TableRow>
                        <TableHead>Plate Number</TableHead>
                        <TableHead>Status</TableHead>
                        <TableHead>Start Time</TableHead>
                        <TableHead>End Time</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {sessions.map((session) => (
                        <TableRow key={session.id}>
                            <TableCell className="font-medium">{session.plateNumber}</TableCell>
                            <TableCell>{session.status}</TableCell>
                            <TableCell>{session.startTime ?<PrettyDate timestamp={session.startTime}/> : "N/A"}</TableCell>
                            <TableCell>{session.endTime ?<PrettyDate timestamp={session.endTime}/> : "N/A"}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                {/*<TableFooter>*/}
                {/*    <TableRow>*/}
                {/*        <TableCell colSpan={3}>Total</TableCell>*/}
                {/*        <TableCell className="text-right">$2,500.00</TableCell>*/}
                {/*    </TableRow>*/}
                {/*</TableFooter>*/}
            </Table>}
        </CardContent>
        <CardFooter className="flex justify-center items-center">
            <Button>
                See all sessions
            </Button>
        </CardFooter>
    </Card>)
}

export default DashboardRecentSessions;