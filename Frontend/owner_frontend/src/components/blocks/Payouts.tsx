import {ColumnDef} from "@tanstack/react-table";
import PrettyDate from "@/components/ui/pretty-date.tsx";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {IPage} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {motion} from "framer-motion";
import SmallLoader from "@/components/ui/small-loader.tsx";
import DataTable from "@/components/blocks/tables/DataTable.tsx";
import {observer} from "mobx-react-lite";
import {ETransactionStatus} from "@/models/common/ETransactionStatus.ts";

interface IPayout{
    id: string;
    status: ETransactionStatus;
    dateCreated: number[];
    dateUpdated?: number[];
    bankAccount: string;
    amount: string;
}

const columns: ColumnDef<IPayout>[] = [

    {
        accessorKey: "status",
        header: "Status"
        ,
        cell: ({row}) =>{
            const rowStatus:ETransactionStatus = row.getValue("status");
            return <div className={`${rowStatus == ETransactionStatus.COMPLETED ? "text-green-800" : rowStatus == ETransactionStatus.PENDING ? "text-yellow-800" : "text-rose-800"}`}>{rowStatus}</div>
        }
    },
    {
        accessorKey: "dateCreated",
        header: "Creation Date",
        cell: ({row}) =>{
            return <div className="">{row.getValue("dateCreated") ?<PrettyDate timestamp={row.getValue("dateCreated")} /> : "N/A" }</div>
        }
    },
    {
        accessorKey: "dateUpdated",
        header: "Last Updated",
        cell: ({row}) =>{
            return <div className="">{row.getValue("dateUpdated") ? <PrettyDate timestamp={row.getValue("dateUpdated")} /> : "N/A" }</div>
        }
    },
    {
        accessorKey: "bankAccount",
        header: "Bank Account Name",
    },
    {
        accessorKey: "amount",
        header: "Amount",
    }
]


const  Payouts = observer(() => {

    const {paymentsStore} = useContext(Context);


    const [payouts, setPayouts] = useState<IPage<IPayout>>({} as IPage<IPayout>);

    const [page, setPage] = useState(0);
    const [filter, setFilter] = useState("");
    const [pageSize, setPageSize] = useState(10);
    const [debouncedFilter, setDebouncedFilter] = useState("");

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const handler = setTimeout(() => {
            setDebouncedFilter(filter);
        }, 1000); // 1 second delay

        return () => clearTimeout(handler); // Cleanup previous timeout
    }, [filter]);

    useEffect(() => {
        if (debouncedFilter) {
            console.log("Change filter",debouncedFilter);
        }
    }, [debouncedFilter]);

    useEffect(() => {
        console.log("Change page size, page or filter",page,pageSize);
    }, [page,pageSize]);

    useEffect(() => {
        setPayouts(paymentsStore.payouts)
    }, [paymentsStore.payouts]);



    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {


                const toastMessage: IToastInfo | void = await paymentsStore.getPayouts("owner",page, pageSize, debouncedFilter);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }


                console.log(paymentsStore.payouts)

                if (paymentsStore.payouts.content) {
                    setPayouts(paymentsStore.payouts as IPage<IPayout>);
                    console.log("Payouts page:", paymentsStore.payouts);
                }

            } catch (error) {
                console.error("Failed to fetch payouts data:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [paymentsStore, page, pageSize, debouncedFilter]);

    return (
        // <div className="w-full bg-white rounded-lg shadow-sm p-6 flex flex-col gap-4">
        //     <Table className="border border-gray-200  " >
        //         <TableCaption>A list of your recent invoices.</TableCaption>
        //         <TableHeader>
        //             <TableRow>
        //                 <TableHead className="w-[100px]">Invoice</TableHead>
        //                 <TableHead>Status</TableHead>
        //                 <TableHead>Method</TableHead>
        //                 <TableHead className="text-right">Amount</TableHead>
        //             </TableRow>
        //         </TableHeader>
        //         <TableBody>
        //             <TableRow>
        //                 <TableCell className="font-medium">INV001</TableCell>
        //                 <TableCell>Paid</TableCell>
        //                 <TableCell>Credit Card</TableCell>
        //                 <TableCell className="text-right">$250.00</TableCell>
        //             </TableRow>
        //         </TableBody>
        //     </Table>
        //  </div>
        <motion.div className="mx-auto mt-6 p-6 bg-white rounded-lg shadow"
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}>
            {loading ? <SmallLoader className={"min-h-[300px]"}/>
                :<DataTable<IPayout> pageData={payouts} columns={columns}  setPage={setPage} filter={filter} setFilter={setFilter} setPageSize={setPageSize} />
            }
        </motion.div>
            )
});

export default Payouts;