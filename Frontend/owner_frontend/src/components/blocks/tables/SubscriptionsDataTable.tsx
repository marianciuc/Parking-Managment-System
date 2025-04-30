import React from 'react';
import {
    ColumnDef,
    flexRender,
    getCoreRowModel,
    useReactTable,
} from '@tanstack/react-table';
import { ITariffRecord } from '@/models/common/ITariffRecord';
import { Table, TableBody, TableCell, TableHeader, TableRow } from '@/components/ui/table';
import { CreateNewTariffDialog } from '@/components/dialogs/CreateNewTariffDialog';
import PrettyDate from "@/components/ui/pretty-date.tsx";
import {EditTariffDialog} from "@/components/dialogs/EditTariffDialog.tsx";
import { motion } from 'framer-motion';

interface ActionButtonsProps {
    onAddTariff?: (tariff: ITariffRecord) => void;
}

const ActionButtons: React.FC<ActionButtonsProps> = ({ onAddTariff }) => (
    <div className="flex justify-end">
        <CreateNewTariffDialog onAddTariff={onAddTariff} />
    </div>
);

const columns: Array<ColumnDef<ITariffRecord>> = [
    {
        accessorKey: 'name',
        header: () => (
            <span>
                Tariff Name
            </span>
        ),
        cell: ({ row }) => <div className="text-sm">{String(row.getValue('name'))}</div>,
    },
    {
        accessorKey: 'tariffClass',
        header: 'Class',
        cell: ({ row }) => <div className="text-sm">{row.original.tariffClass}</div>,
    },
    {
        accessorKey: 'price',
        header : 'Price',
        cell: ({ row }) => <div className="text-sm">{row.original.price.toFixed(2)}</div>,

    },
    {
        accessorKey: 'currency',
        header: 'Currency',
        cell: ({ row }) => <div className="text-sm">{row.original.currency}</div>,

    },
    {
        accessorKey: 'modificationDate',
        header: 'Last Modified',
        cell: ({ row }) => (
            <>
                {row.original.modificationDate
                    ? <PrettyDate timestamp={row.original.modificationDate} />
                    : 'N/A'}
            </>
        ),
    },
    {
        id: 'actions',
        header: () => <span>Actions</span>,
        cell: ({ row }) => {
            const record = row.original;
            return <EditTariffDialog tariffRecord={record}/>
        },
    },
];

interface SubscriptionsDataTableProps {
    data?: ITariffRecord[];
    onAddTariff?: (tariff: ITariffRecord) => void;
}

const SubscriptionsDataTable: React.FC<SubscriptionsDataTableProps> = ({
                                                                           data = [],
                                                                           onAddTariff,
                                                                       }) => {


    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
    });

    return (
        <motion.div className="space-y-4"
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.3 }}>
            <div className="flex justify-end items-center ">

                <ActionButtons onAddTariff={onAddTariff} />
            </div>
            <div className="rounded-md border shadow-sm">
                <Table>
                    <TableHeader>
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id} className="w-full">
                                {headerGroup.headers.map((header) => (
                                    <TableCell key={header.id} className="font-medium text-gray-800 grow">
                                        {header.isPlaceholder
                                            ? null
                                            : flexRender(header.column.columnDef.header, header.getContext())}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableHeader>
                    <TableBody>
                        {table.getRowModel().rows.length ? (
                            table.getRowModel().rows.map((row) => (
                                <TableRow
                                    key={row.id}
                                    data-state={row.getIsSelected() ? 'selected' : undefined}
                                    className={row.getIsSelected() ? 'bg-gray-100' : undefined}
                                >
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell key={cell.id}>
                                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell colSpan={columns.length} className="text-center py-4">
                                    No results found.
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </div>
        </motion.div>
    );
};

export default SubscriptionsDataTable;