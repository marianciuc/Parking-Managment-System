import DataTable from "@/components/blocks/tables/DataTable.tsx";
import {useParams} from "react-router-dom";
import  {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import { IWhiteListRecord} from "@/models/common/dataTables";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {IPage} from "@/models/common";
import {ColumnDef} from "@tanstack/react-table";
import {Button} from "@/components/ui/button.tsx";
import { ShieldCheck} from "lucide-react";
import PrettyDate from "@/components/ui/pretty-date.tsx";

import CreateWhiteListRecordDialog from "@/components/dialogs/CreateWhiteListRecordDialog.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";
import EditWhiteListRecordDialog from "@/components/dialogs/EditWhiteListRecord.tsx";
import { motion } from "framer-motion";
import {observer} from "mobx-react-lite";



const columns: ColumnDef<IWhiteListRecord>[] = [

    {
        accessorKey: "vehiclePlate",
        header: "Car Number"
        ,
        cell: ({row}) =>{
            return <div className="">{row.getValue("vehiclePlate")}</div>
        }
    },
    {
        accessorKey: "tariffName",
        header: "Tariff Name",
    },
    {
        accessorKey: "createdAt",
        header: "Creation Date",
        cell: ({row}) =>{
            return <div className="">{ row.getValue("createdAt") ? <PrettyDate timestamp={row.getValue("createdAt")} /> : "N/A"}</div>
        }
    },
    {
        accessorKey: "updatedAt",
        header: "Last Modification Date",
        cell: ({row}) =>{
            return <div className="">{ row.getValue("updatedAt") ? <PrettyDate timestamp={row.getValue("updatedAt")} /> : "N/A"}</div>
        }
    },
    {
        id: "actions",
        cell: ({ row }) => {
            const record = row.original

            return <EditWhiteListRecordDialog record={record}/>
        },
    }
]

function Buttons(){
    return (
        <div className="flex flex-row justify-end w-full gap-2 ">
            <Button>
                <ShieldCheck />
                Enable only whitelist mode
            </Button>
            <CreateWhiteListRecordDialog/>
        </div>
    )
}

const WhiteList  = observer(()=> {

    const {parkingId} = useParams<{ parkingId: string }>();


    const {accessManagementStore} = useContext(Context);

    const [loading, setLoading] = useState<boolean>(false);



    const [whitelist, setWhitelist] = useState<IPage<IWhiteListRecord>>({} as IPage<IWhiteListRecord>);

    const [page, setPage] = useState(0);
    const [filter, setFilter] = useState("");
    const [pageSize, setPageSize] = useState(10);

    const [debouncedFilter, setDebouncedFilter] = useState("");

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
        setWhitelist(accessManagementStore.page as IPage<IWhiteListRecord>)
    }, [accessManagementStore.page]);




    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching access list for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await accessManagementStore.fetchAccessData(parkingId, false, page, pageSize, debouncedFilter);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }



                if (accessManagementStore.page.content) {
                    setWhitelist(accessManagementStore.page as IPage<IWhiteListRecord>);
                }

            } catch (error) {
                console.error("Failed to fetch access list data:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, accessManagementStore, page, pageSize, debouncedFilter]);

    // useEffect(() => {
    //     setWhitelist(accessManagementStore.page as IPage<IWhiteListRecord>);
    // }, [accessManagementStore.page.content, accessManagementStore.page]);


    return (
        <motion.div className="mx-auto mt-6 p-6 bg-white rounded-lg shadow"
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}>
            {loading ? <SmallLoader className={"min-h-[300px]"}/>
                :<DataTable<IWhiteListRecord> pageData={whitelist} columns={columns} actions={<Buttons/>} setPage={setPage} filter={filter} setFilter={setFilter} setPageSize={setPageSize} />
            }
        </motion.div>
    )
});

export default WhiteList;