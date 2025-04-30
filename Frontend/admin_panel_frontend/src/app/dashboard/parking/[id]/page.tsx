"use client";

import React, {useContext, useEffect, useState} from "react";

import {Input} from "@/components/ui/input";
import {Context} from "@/providers/ClientContextProvider";
import {OwnerDomain, Parking, UserDomain} from "@/interfaces";
import {toast} from "sonner";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import {Label} from "@/components/ui/label";
import {ChevronRightIcon, OctagonAlert} from "lucide-react";
import {Table, TableBody, TableCell, TableRow} from "@/components/ui/table";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import ApiKeys from "@/components/api-keys";

export default function OwnersDetailsPage({
                                              params
                                          }: { params: Promise<{ id: string }> }) {

    const {parkingStore, userDomainStore} = useContext(Context);
    const [loading, setLoading] = useState(false);
    const [parking, setParking] = useState<Parking | null>(null);

    useEffect(() => {
        (async () => {
            const {id} = await params;
            if (id) {
                fetchParking(id)
            }
        })();
    }, [params]);


    const fetchParking = async (id: string) => {
        setLoading(true);
        parkingStore.fetchOwnerDetails(id).then(res => {
            if (res) {
                toast.error("Error!", {description: res.message});
            } else {
                setParking(parkingStore.currentParkingDetails);
            }
            setLoading(false);

        })
    }

    return (
        <div>
            {parking && <>
                <h1 className="text-2xl font-bold mb-4">Parking Management</h1>

                <div className="card p-4">
                    <h2>Details</h2>
                    <div className="flex gap-10">
                        <div className="w-[300px] inline-block">
                            <Table className="border-collapse">
                                <TableBody>
                                    <TableRow>
                                        <TableCell className="text-right w-full">Name: </TableCell>
                                        <TableCell className="text-left">{parking.name}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell className="text-right">Record Status: </TableCell>
                                        <TableCell className="text-left">{parking.recordStatus}</TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell className="text-right">Address: </TableCell>
                                        <TableCell
                                            className="text-left">
                                            {parking.address.countryCode},
                                            {parking.address.city},
                                            {parking.address.buildingNumber},
                                            {parking.address.street},
                                            {parking.address.buildingNumber} ({parking.address.postalCode})
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell className="text-right">Capacity: </TableCell>
                                        <TableCell
                                            className="text-left">
                                            {parking.capacity.occupiedSpaces}/{parking.capacity.capacity}
                                        </TableCell>
                                    </TableRow>
                                    <TableRow>
                                        <TableCell className="text-right"> Status: </TableCell>
                                        <TableCell
                                            className="text-left">
                                            {parking.parkingStatus}
                                        </TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </div>
                        <div className="flex flex-col w-[300px] gap-4">
                            <Button>Complete Parking creation <OctagonAlert/></Button>
                            <Button>Delete Parking <OctagonAlert/></Button>
                            <Button>Temporary close <ChevronRightIcon/></Button>
                        </div>
                    </div>
                </div>
                <div className="mt-5 card p-4 flex gap-4 items-center justify-center content-center w-full">
                    <Tabs defaultValue="api-keys" className="w-full">
                        <TabsList>
                            <TabsTrigger value="api-keys">Api Keys</TabsTrigger>
                            <TabsTrigger value="sessions">Sessions</TabsTrigger>
                            <TabsTrigger value="transactions">Transactions</TabsTrigger>
                            <TabsTrigger value="access-list">Access List</TabsTrigger>
                        </TabsList>
                        <TabsContent value="api-keys">
                            <ApiKeys id={parking.id}/>
                        </TabsContent>
                        <TabsContent value="sessions">Change your password here.</TabsContent>
                    </Tabs>
                </div>
            </>}
        </div>
    );
}