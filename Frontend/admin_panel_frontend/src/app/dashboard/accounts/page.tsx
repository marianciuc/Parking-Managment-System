"use client";

import React, {useContext, useEffect, useState} from "react";


import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger,} from "@/components/ui/dropdown-menu"

import {Input} from "@/components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue,} from "@/components/ui/select"
import {Context} from "@/providers/ClientContextProvider";
import {UserDomain} from "@/interfaces";
import {toast} from "sonner";
import {UserType} from "@/interfaces/user-domain";
import {Button} from "@/components/ui/button";
import Link from "next/link";


export default function UsersManagementPage() {

    const {userDomainStore} = useContext(Context);
    const [loading, setLoading] = useState(false);
    const [users, setUsers] = useState<UserDomain[]>([]);
    const [page, setPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const [email, setEmail] = useState("");
    const [userType, setUserType] = useState("");
    const [recordStatus, setRecordStatus] = useState("");

    const fetchUsers = async () => {
        setLoading(true);
        userDomainStore.searchOwners({
            email,
            page: page - 1,
            size: perPage,
            recordStatus,
            userType
        }).then(res => {
            if (res) {
                toast.error("Error! You have entered incorrect login or password.", {description: res.message});
            } else {
                setUsers(userDomainStore.getUsers);
            }
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchUsers();
    }, [email, userType, recordStatus, page, perPage]);

    return (
        <div>
            <h1 className="text-2xl font-bold mb-4">Users Management</h1>

            <div className="card p-3 mb-2">
                <div className="flex gap-4 container">
                    <Input placeholder={"Search by email"} onChange={(e) => setEmail(e.target.value)}/>
                    <Select>
                        <SelectTrigger className="w-[180px]">
                            <SelectValue placeholder="Select User Type"/>
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="all">All</SelectItem>
                            <SelectItem value="ADMIN">Admin</SelectItem>
                            <SelectItem value="DRIVER">Driver</SelectItem>
                            <SelectItem value="PARKING_OWNER">Parking Owner</SelectItem>
                        </SelectContent>
                    </Select>
                    <Select>
                        <SelectTrigger className="w-[180px]">
                            <SelectValue placeholder="Select Record Status"/>
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="all">All</SelectItem>
                            <SelectItem value="ACTIVE">Active</SelectItem>
                            <SelectItem value="BANNED">Banned</SelectItem>
                            <SelectItem value="DELETED">Deleted</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </div>
            <div className="card p-3">
                {loading && <div>Loading...</div>}
                <Table>
                    <TableCaption>A list of your recent invoices.</TableCaption>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[200px]">Email</TableHead>
                            <TableHead>User Type</TableHead>
                            <TableHead>Registration Date</TableHead>
                            <TableHead>Modification Date</TableHead>
                            <TableHead>Record Status</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    {users && <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id}>
                                <TableCell className="font-medium">{user.email}</TableCell>
                                <TableCell>{user.userType}</TableCell>
                                <TableCell>{user.creationDate.toLocaleString()}</TableCell>
                                <TableCell>{user.lastModifiedDate.toLocaleString()}</TableCell>
                                <TableCell>{user.recordStatus}</TableCell>
                                <TableCell className="text-right">
                                    <DropdownMenu>
                                        <DropdownMenuTrigger><Button>More</Button></DropdownMenuTrigger>
                                        <DropdownMenuContent>
                                            <DropdownMenuItem>Transactions</DropdownMenuItem>
                                            <DropdownMenuItem> <Link
                                                href={`/dashboard/accounts/${user.userType == UserType.ADMIN ? "administrators" : user.userType == UserType.OWNER ? "owners" : "drivers"}/${user.id}`}
                                                className="flex items-center gap-3 px-4 py-2 rounded hover:bg-gray-700">
                                                Details
                                            </Link></DropdownMenuItem>
                                            {user.userType == UserType.OWNER &&
                                                <DropdownMenuItem>Parking list</DropdownMenuItem>}
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>}
                </Table>
            </div>
        </div>
    )
        ;
}