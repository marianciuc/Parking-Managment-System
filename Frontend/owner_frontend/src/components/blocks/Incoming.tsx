import {ColumnDef} from "@tanstack/react-table";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {IPage} from "@/models/common";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {motion} from "framer-motion";
import SmallLoader from "@/components/ui/small-loader.tsx";
import DataTable from "@/components/blocks/tables/DataTable.tsx";

interface IIncoming{
    id: string;
    status: string;
    method:string;
    description: string;
    amount: string;
}

const columns: ColumnDef<IIncoming>[] = [

    {
        accessorKey: "status",
        header: "Status"
        ,
        cell: ({row}) =>{
            return <div className="">{row.getValue("status")}</div>
        }
    },
    {
        accessorKey: "description",
        header: "Parking",
    },
    {
        accessorKey: "method",
        header: "Method",
    },
    {
        accessorKey: "amount",
        header: "Amount",
    }
]


export default function Incoming(){

    const {paymentsStore} = useContext(Context);


    const [incoming, setIncoming] = useState<IPage<IIncoming>>({} as IPage<IIncoming>);

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
        const fetchData = async () => {
            setLoading(true);
            try {


                const toastMessage: IToastInfo | void = await paymentsStore.getIncoming("owner",page, pageSize, debouncedFilter);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }


                console.log(paymentsStore.payouts)

                if (paymentsStore.incoming.content) {
                    setIncoming(paymentsStore.incoming as IPage<IIncoming>);
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
                :<DataTable<IIncoming> pageData={incoming} columns={columns}  setPage={setPage} filter={filter} setFilter={setFilter} setPageSize={setPageSize} />
            }
        </motion.div>
    )
}