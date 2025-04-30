import {Card, CardContent, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {DollarSign} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useState} from "react";
import PrettyDate from "@/components/ui/pretty-date.tsx";
import {IParking} from "@/models/common";
import { motion } from "framer-motion";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";


const payments = [
    {id:1, status:"COMPLETED", date : [2025, 4, 1, 12,0, 0, 1], amount: 30.00},
    {id:2,status:"COMPLETED", date : [2025, 3, 1, 12,0, 0, 1], amount: 30.00},
]

function DashboardSubscriptionsInformation({parking}:{parking:IParking}) {

    const [currentSubscription] = useState({
        name: "Standard subscription",
        price: "30$/monthly",
        dateOfExpire: [2025,12, 11,20,4,5,4],
    });

    console.log(parking)
    return (<motion.div className="mb-12 flex justify-center flex-col gap-10"
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}>
        <Card className="relative bg-gradient-to-br from-white/80 to-gray-200 shadow-xl rounded-2xl border border-gray-300/40 backdrop-blur-lg p-6 w-full">
            <CardHeader className="flex flex-col items-center text-center">
                <div className="bg-white/60 p-3 rounded-full shadow-md">
                    <DollarSign className="w-8 h-8 text-gray-800" />
                </div>
                <CardTitle className="mt-4 text-gray-900 text-lg font-semibold tracking-wide">
                    {currentSubscription.name}
                </CardTitle>
            </CardHeader>
            <CardContent className="text-center">
                <p className="text-sm text-gray-600">Cost:</p>
                <p className="text-3xl font-semibold text-gray-900">{currentSubscription.price}</p>
                <p className="mt-4 text-xs text-gray-500">
                    Subscription is valid until: <span className="font-medium text-gray-900"><PrettyDate timestamp={currentSubscription.dateOfExpire}/> </span>
                </p>
                <Button
                    variant="secondary"
                    className="mt-6 bg-gray-900 text-white py-2 px-6 rounded-full hover:opacity-80 transition-opacity shadow-md">
                    Change tariff
                </Button>
            </CardContent>
        </Card>
        <Card>
            <CardHeader>
                <CardTitle>Subscription Payments</CardTitle>
            </CardHeader>
            <CardContent>
                <Table>
                    {/*<TableCaption>Recent sessions</TableCaption>*/}
                    <TableHeader>
                        <TableRow>
                            <TableHead>Status</TableHead>
                            <TableHead>Date</TableHead>
                            <TableHead>Amount</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {payments.map((session) => (
                            <TableRow key={session.id}>
                                <TableCell className="font-medium text-green-800">{session.status}</TableCell>
                                <TableCell><PrettyDate timestamp={session.date}/></TableCell>
                                <TableCell>{session.amount}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                    {/*<TableFooter>*/}
                    {/*    <TableRow>*/}
                    {/*        <TableCell colSpan={3}>Total</TableCell>*/}
                    {/*        <TableCell className="text-right">$2,500.00</TableCell>*/}
                    {/*    </TableRow>*/}
                    {/*</TableFooter>*/}
                </Table>
            </CardContent>
            <CardFooter className="flex justify-center items-center">
                {/*<Button>*/}
                {/*    See all sessions*/}
                {/*</Button>*/}
            </CardFooter>
        </Card>
    </motion.div>)
}
export default DashboardSubscriptionsInformation;