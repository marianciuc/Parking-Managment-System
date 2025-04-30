import {useContext, useState} from 'react';
import {
    ColumnDef,
} from '@tanstack/react-table';
import {IPage, ISessionRecord} from '@/models/common';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';

import { MoreHorizontal } from 'lucide-react';
import {useEffect} from 'react';
import SessionDetailsDialog from "@/components/dialogs/SessionDetailsDialog.tsx";
import PrettyDate from "@/components/ui/pretty-date.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";
import DataTable from "@/components/blocks/tables/DataTable.tsx";
import {Context} from "@/main.tsx";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {useParams} from "react-router-dom";


const columns: Array<ColumnDef<ISessionRecord>> = [
    {
        accessorKey: 'status',
        header: () => (
            <span>
                Status
            </span>
        ),
        cell: ({ row }) => <div className={`text-sm ${row.original.status === "ACTIVE" ?"text-green-400" :

            row.original.status === "FINISHED" ? "text-red-400" : ""}`}>{String(row.getValue('status'))}</div>,
    },
    {
        accessorKey: 'plateNumber',
        header: 'Vehicle Number',
        cell: ({ row }) => <div className="text-sm">{row.original.plateNumber}</div>,
    },
    {
        accessorKey: 'tariffName',
        header: 'Tariff',
        cell: ({ row }) => <div className="text-sm">{row.original.tariffName}</div>,
    },
    {
        accessorKey: 'accessList',
        header: 'Access List',
        cell: ({ row }) => <div className="text-sm">{row.original.accessList}</div>,
    },
    {
        accessorKey: 'startTime',
        header: 'Start Time',
        cell: ({ row }) => (
            <>
                {row.original.startTime
                    ? <PrettyDate timestamp={row.original.startTime} />
                    : 'N/A'}
            </>
        ),
    },

    {
        accessorKey: 'endTime',
        header: 'End Time',
        cell: ({ row }) => (
            <>
                {row.original.endTime
                    ? <PrettyDate timestamp={row.original.endTime} />
                    : 'N/A'}
            </>
        ),
    },

    {
        accessorKey: 'paymentStatus',
        header: 'Payment Status',
        cell: ({ row }) => <div className={`text-sm ${row.original.paymentStatus === "PAID" ?"text-green-400" :

            row.original.paymentStatus === "UNPAID" ? "text-red-400" : ""}`}>{row.original.paymentStatus}</div>,

    },

    {
        id: 'actions',
        header: () => <span>Actions</span>,
        cell: ({ row }) => {
            const record = row.original;
            return (
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="ghost" className="h-8 w-8 p-0">
                            <span className="sr-only">Open menu</span>
                            <MoreHorizontal className="h-4 w-4" />
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                        <DropdownMenuLabel>Actions</DropdownMenuLabel>
                        <DropdownMenuItem
                            onClick={() =>
                                navigator.clipboard
                                    .writeText(record.plateNumber)
                                    .then(() => alert('Vehicle number copied!'))
                            }
                        >
                            Copy Vehicle Number
                        </DropdownMenuItem>
                        <DropdownMenuSeparator />
                        <DropdownMenuItem asChild>
                            <SessionDetailsDialog sessionId={record.id} />

                        </DropdownMenuItem>
                        <DropdownMenuItem
                            onClick={() => {
                                if (confirm('Are you sure you want to stop this session?')) {
                                    alert('Stop successfully');
                                }
                            }}
                        >
                            Stop
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            );
        },
    },
];



const SessionDataTable = () => {


    const {parkingId} = useParams<{ parkingId: string }>();

    const {sessionStore} = useContext(Context);

    const [sessions, setSessions] = useState<IPage<ISessionRecord>>({} as IPage<ISessionRecord>);

    const [page, setPage] = useState(0);
    const [filter, setFilter] = useState("");
    const [pageSize, setPageSize] = useState(10);

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

                const toastMessage: IToastInfo | void = await sessionStore.fetchSessionsData(parkingId,page,pageSize);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }


                // Ensure sessionStore.sessions is populated before updating state
                if (sessionStore.sessionsPage) {
                    setSessions(sessionStore.sessionsPage);
                }


            } catch (error) {
                console.error("Failed to fetch sessions:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [page, pageSize, parkingId, sessionStore]);


    return (
        <div className="py-10 px-6">
            {loading ? <SmallLoader className={"min-h-[300px]"}/>
                :<DataTable<ISessionRecord> pageData={sessions} columns={columns} actions={<div/>} setPage={setPage} filter={filter} setFilter={setFilter} setPageSize={setPageSize} />
            }
        </div>
    )
};

export default SessionDataTable;