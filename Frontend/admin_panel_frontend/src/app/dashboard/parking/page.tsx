"use client";

import React, {useContext, useEffect, useState} from "react";

import {Input} from "@/components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Label} from "@/components/ui/label";
import {useSearchParams} from "next/navigation";
import {Context} from "@/providers/ClientContextProvider";
import {ParkingListItem} from "@/interfaces";
import {toast} from "sonner";
import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import Link from "next/link";
import {Button} from "@/components/ui/button";


export default function ParkingManagementPage() {
    const searchParams = useSearchParams();
    const ownerIdQuery = searchParams.get("ownerId");
    const {parkingStore} = useContext(Context);

    const [ownerId, setOwnerId] = useState(ownerIdQuery || "");
    const [parkingName, setParkingName] = useState("");
    const [parkingId, setParkingId] = useState("");
    const [parkingStatus, setParkingStatus] = useState("");
    const [recordStatus, setRecordStatus] = useState("");
    const [country, setCountry] = useState("");
    const [city, setCity] = useState("");
    const [parkingType, setParkingType] = useState("");
    const [name, setName] = useState("");

    const [page, setPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const [parkings, setParkings] = useState<ParkingListItem[]>([]);

    const [loading, setLoading] = useState(false);

    const fetchParking = async () => {
        parkingStore.searchOwners({
            page: page - 1,
            size: perPage,
            id: parkingId,
            country: country,
            city: city,
            ownerId,
            recordStatus: recordStatus,
            status: parkingStatus,
            name
        }).then(res => {
            if (res) {
                toast.error("Error!", {description: res.message});
            } else {
                setParkings(parkingStore.getParkingList);
            }
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchParking();
    }, [ownerId, parkingName, parkingId, parkingStatus, recordStatus, country, city, parkingType, name, page, perPage]);


    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">Parking Management</h1>

            <div className="card p-3 mb-2">
                <div className="container">
                    <div className="flex gap-4 mb-4 items-end">
                        <div className="w-[300px]">
                            <Label>Search by parking name</Label>
                            <Input placeholder={"Search by Owner ID"} value={ownerId} onChange={(e) => {
                                setOwnerId(e.target.value);
                            }}/>
                        </div>
                        <Select>
                            <SelectTrigger className="w-[180px]">
                                <SelectValue placeholder="Country"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">All</SelectItem>
                                <SelectItem value="ADMIN">Open</SelectItem>
                                <SelectItem value="DRIVER">Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Temporary Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Creation Proccess</SelectItem>
                            </SelectContent>
                        </Select>
                        <Select>
                            <SelectTrigger className="w-[180px]">
                                <SelectValue placeholder="City"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">All</SelectItem>
                                <SelectItem value="ADMIN">Open</SelectItem>
                                <SelectItem value="DRIVER">Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Temporary Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Creation Proccess</SelectItem>
                            </SelectContent>
                        </Select>
                        <Select>
                            <SelectTrigger className="w-[180px]">
                                <SelectValue placeholder="STATUS"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">All</SelectItem>
                                <SelectItem value="ADMIN">Open</SelectItem>
                                <SelectItem value="DRIVER">Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Temporary Closed</SelectItem>
                                <SelectItem value="PARKING_OWNER">Creation Process</SelectItem>
                            </SelectContent>
                        </Select>
                        <Select>
                            <SelectTrigger className="w-[180px]">
                                <SelectValue placeholder="Select Record Status"/>
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="all">All</SelectItem>
                                <SelectItem value="ACTIVE">Active</SelectItem>
                                <SelectItem value="DELETED">Deleted</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="flex gap-4">
                        <div className="w-[300px]">
                            <Label>Search by parking name</Label>
                            <Input placeholder={"Search by Parking name"} onChange={(e) => {
                            }}/>
                        </div>
                        <div className="w-[300px]">
                            <Label>Search by parking id</Label>
                            <Input placeholder={"Search by Parking name"} onChange={(e) => {
                            }}/>
                        </div>
                    </div>
                </div>
            </div>

            <div className="card p-3">
                <div className="container">
                    <Table>
                        <TableCaption>A list of your recent invoices.</TableCaption>
                        <TableHeader>
                            <TableRow>
                                <TableHead className="w-[200px]">Name</TableHead>
                                <TableHead>Status</TableHead>
                                <TableHead>Occupied Slots</TableHead>
                                <TableHead>Record Status</TableHead>
                                <TableHead>Address</TableHead>
                                <TableHead className="text-right">Actions</TableHead>
                            </TableRow>
                        </TableHeader>
                        {parkings && <TableBody>
                            {parkings.map((parking: ParkingListItem) => (
                                <TableRow key={parking.id}>
                                    <TableCell className="font-medium">{parking.name}</TableCell>
                                    <TableCell>{parking.parkingStatus}</TableCell>
                                    <TableCell>{parking.occupiedSlots}/{parking.capacity}</TableCell>
                                    <TableCell>{parking.recordStatus}</TableCell>
                                    <TableCell>{parking.address.countryCode}, {parking.address.city}, {parking.address.buildingNumber}</TableCell>
                                    <TableCell><Link href={`/dashboard/parking/${parking.id}`}>Show
                                        more</Link></TableCell>
                                </TableRow>
                            ))}
                        </TableBody>}
                    </Table>
                </div>

                {loading && <div>Loading...</div>}

                <Button className="mt-4 mr-4" disabled={page <= 1} onClick={() => {
                    setPage(page - 1);
                }}>Prev</Button>
                <Button className="mt-4" onClick={() => {
                    setPage(page + 1);
                }}>Next</Button>
            </div>
        </div>
    );
}

