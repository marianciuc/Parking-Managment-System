import {
    ColumnDef,
     flexRender,
    getCoreRowModel,
    useReactTable,
} from "@tanstack/react-table";
import { ArrowUpNarrowWide,} from "lucide-react"
import { Button } from "@/components/ui/button"
import React, {useEffect, useState} from "react";
import {Input} from "@/components/ui/input.tsx";
import {Table, TableBody, TableCell, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {IPage} from "@/models/common";







interface DataTableProps<T>{
    pageData: IPage<T>;
    columns: ColumnDef<T>[];
    actions?: React.ReactNode;
    setPage: (page: number) => void;
    filter: string;
    setFilter: (filter: string) => void;
    setPageSize: (pageSize: number) => void;


}

const DataTable = <T,>({
                                    pageData,
                                    columns,
                                    actions,
                                    setPage,
                                    // setPageSize,
                                    filter,
                                    setFilter
                                }: DataTableProps<T>) => {

    // const data = Array.isArray(pageData.content) ? pageData.content : [];

    const [data, setData] = useState<T[]>(pageData.content ?? [])

    useEffect(() => {
        setData(pageData.content ?? []);
    }, [pageData.content]); // Update local state when pageData changes

    const table    = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
    });
    console.log(pageData);

    return (
        <div className="space-y-4 w-full min-h-36">
            <div className="flex justify-start items-center gap-6">
                <Input
                    placeholder="Filter results..."
                    value={filter ?? ''}
                    onChange={(event) =>
                        setFilter(event.target.value)
                    }
                    className="max-w-sm"
                />
                <ArrowUpNarrowWide />
                {actions}
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
                <div className="p-4">
                    <Button
                        variant="ghost"
                        disabled={pageData.first}
                        onClick={() => setPage(pageData.number - 1)}
                    >
                        Previous
                    </Button>
                    <Button
                        variant="ghost"
                        disabled={pageData.last}
                        onClick={() => setPage(pageData.number + 1)}
                    >
                        Next
                    </Button>
                    <span className="text-sm">
                        {/*Page {table.getState().pagination.pageIndex + 1} of {table.getPageCount() !== 0 ? table.getPageCount() : 1}*/}
                        Page {pageData.number ? pageData.number + 1 : 1} of {pageData.totalPages ? pageData.totalPages : 1}
                    </span>
                </div>
            </div>
        </div>
    );
};

export default DataTable;