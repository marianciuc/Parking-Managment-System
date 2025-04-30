import DataTable from "@/components/blocks/tables/DataTable.tsx";
import {useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {IBlackListRecord} from "@/models/common/dataTables";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {IPage} from "@/models/common";
import {ColumnDef} from "@tanstack/react-table";
import PrettyDate from "@/components/ui/pretty-date.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";
import CreateBlackListRecordDialog from "@/components/dialogs/CreateBlackListRecord.tsx";
import EditBlackListRecordDialog from "@/components/dialogs/EditBlackListRecord.tsx";
import { motion } from "framer-motion";



const columns: ColumnDef<IBlackListRecord>[] = [
    {
        accessorKey: "plateNumber",
        header: "Car Number"
        ,
        cell: ({row}) =>{
            return <div className="">{row.getValue("plateNumber")}</div>
        }
    },
    {
        accessorKey: "reason",
        header: "Reason",
    },
    {
        accessorKey: "createdAt",
        header: "Creation Date",
        cell: ({row}) =>{
            return <div className=""><PrettyDate timestamp={row.getValue("createdAt")} /></div>
        }
    },
    {
        accessorKey: "updatedAt",
        header: "Last Modification Date",
        cell: ({row}) =>{
            return <div className=""><PrettyDate timestamp={row.getValue("updatedAt")} /></div>
        }
    },
    {
        id: "actions",
        cell: ({ row }) => {
            const record = row.original

            return <EditBlackListRecordDialog record={record}/>
        },
    }
]

function Buttons( {blackList, setBlackList} : {blackList: IPage<IBlackListRecord>, setBlackList: (page:IPage<IBlackListRecord>)=>void}){

    const {accessManagementStore} = useContext(Context);
    useEffect(() => {
        setBlackList(accessManagementStore.page as IPage<IBlackListRecord>);
        console.log("Buttons change data blacklist")
    }, [blackList, setBlackList, accessManagementStore.page]);

    return (
        <div className="flex flex-row justify-end w-full gap-2 ">
            <CreateBlackListRecordDialog setBlackList={setBlackList} />
        </div>
    )
}




function BlackList(){

    const {parkingId} = useParams<{ parkingId: string }>();


    const {accessManagementStore} = useContext(Context);

    const [loading, setLoading] = useState<boolean>(false);



    const [blacklist, setBlacklist] = useState<IPage<IBlackListRecord>>({} as IPage<IBlackListRecord>);

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
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }

                console.log("Fetching access list for parkingId:", parkingId);

                const toastMessage: IToastInfo | void = await accessManagementStore.fetchAccessData(parkingId, true, page, pageSize, debouncedFilter);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }



                if (accessManagementStore.page.content) {
                    setBlacklist(accessManagementStore.page as IPage<IBlackListRecord>);
                }

            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
            setLoading(false);
        };

        fetchData();
        }, [parkingId, accessManagementStore, page, pageSize, debouncedFilter]);


    useEffect(() => {
        setBlacklist(accessManagementStore.page as IPage<IBlackListRecord>);
        console.log("Changed blacklist");
        console.log(accessManagementStore.pageUpdate);
    }, [accessManagementStore.page, accessManagementStore.pageUpdate, accessManagementStore]);

        return (
            <motion.div className="mx-auto mt-6 p-6 bg-white rounded-lg shadow"
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3 }}>
            {loading ? <SmallLoader className={"min-h-[300px]"}/>
            :<DataTable<IBlackListRecord> pageData={blacklist} columns={columns} actions={<Buttons blackList={blacklist} setBlackList={setBlacklist}/>} setPage={setPage} filter={filter} setFilter={setFilter} setPageSize={setPageSize} />
            }
        </motion.div>
    )
}

export default BlackList;