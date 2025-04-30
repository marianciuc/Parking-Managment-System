"use client";

import React, {useContext, useEffect, useState} from "react";

import {Input} from "@/components/ui/input";
import {Context} from "@/providers/ClientContextProvider";
import {OwnerDomain, UserDomain} from "@/interfaces";
import {toast} from "sonner";
import {Button} from "@/components/ui/button";
import Link from "next/link";
import {Label} from "@/components/ui/label";
import {ChevronRightIcon, OctagonAlert} from "lucide-react";


export default function OwnersDetailsPage({
                                              params
                                          }: { params: Promise<{ id: string }> }) {

    const {ownerStore, userDomainStore} = useContext(Context);
    const [loading, setLoading] = useState(false);
    const [owner, setOwner] = useState<OwnerDomain | null>(null);
    const [userDomain, setUserDomain] = useState<UserDomain | null>();

    useEffect(() => {
        (async () => {
            const {id} = await params;
            if (id) {
                fetchUser(id)
            }
        })();
    }, [params]);


    const fetchUser = async (id: string) => {
        setLoading(true);
        ownerStore.fetchOwnerDetails(id).then(res => {
            if (res) {
                toast.error("Error!", {description: res.message});
            } else {
                setOwner(ownerStore.currentOwnerDetails);
            }
            setLoading(false);

        })
    }

    return (
        <div>
            {(owner && owner.address) && <>
                <h1 className="text-2xl font-bold mb-4">Users Management</h1>

                <div className="card p-4 mb-2 flex gap-4 items-center justify-start content-center">
                    <h1>Actions: </h1>
                    <Button>Block User <OctagonAlert/></Button>
                    <Button><Link href={`/dashboard/parking?ownerId=${owner.userId}`}>See user parking
                        list </Link><ChevronRightIcon/></Button>
                    <Button>Transactions <ChevronRightIcon/></Button>
                </div>

                <div className="card p-4 space-y-4">
                    <h2 className="text-xl font-semibold">Details</h2>
                    <div className="grid grid-cols-3 gap-4">
                        <div>
                            <Label className="text-sm font-medium">Firstname</Label>
                            <Input defaultValue={owner.firstName} disabled={true}/>
                        </div>
                        <div>
                            <Label className="text-sm font-medium">Middle Name</Label>
                            <Input defaultValue={owner.middleName} disabled={true}/>
                        </div>
                        <div>
                            <Label className="text-sm font-medium">Lastname</Label>
                            <Input defaultValue={owner.lastName} disabled={true}/>
                        </div>
                        <div>
                            <Label className="text-sm font-medium">Phone Number</Label>
                            <Input
                                defaultValue={`${owner.phoneCode} ${owner.phoneNumber}`}
                                disabled={true}
                            />
                        </div>
                        <div>
                            <Label className="text-sm font-medium">Date of Birth</Label>
                            <Input
                                defaultValue={new Date(owner.birthDate).toLocaleDateString()}
                                disabled={true}
                            />
                        </div>
                    </div>
                </div>

                <div className="card p-4 space-y-4 mt-2">
                    <h2 className="text-xl font-semibold">Address</h2>
                    <div className="grid grid-cols-3 gap-4">
                        <div>
                            <Label className="text-sm font-medium">Country</Label>
                            <Input defaultValue={owner.address.country} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">City</label>
                            <Input defaultValue={owner.address.city} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">Street</label>
                            <Input defaultValue={owner.address.street} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">Postal Code</label>
                            <Input defaultValue={owner.address.postalCode} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">House Number</label>
                            <Input defaultValue={owner.address.houseNumber} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">Apartment Number</label>
                            <Input defaultValue={owner.address.apartmentNumber} disabled={true}/>
                        </div>
                    </div>
                </div>

                <div className="card p-4 space-y-4 mt-2">
                    <h2 className="text-xl font-semibold">System Information</h2>
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="text-sm font-medium">User ID</label>
                            <Input defaultValue={owner.userId} disabled={true}/>
                        </div>
                        <div>
                            <label className="text-sm font-medium">Registration Completed</label>
                            <Input
                                defaultValue={
                                    owner.isRegistrationCompleted ? "Yes" : "No"
                                }
                                disabled={true}
                            />
                        </div>
                        <div>
                            <label className="text-sm font-medium">Registration Date</label>
                            <Input
                                defaultValue={new Date(owner.creationDate).toLocaleDateString()}
                                disabled={true}
                            />
                        </div>
                        <div>
                            <label className="text-sm font-medium">Last Modification Date</label>
                            <Input
                                defaultValue={new Date(owner.modificationDate).toLocaleDateString()}
                                disabled={true}
                            />
                        </div>
                    </div>
                </div>
            </>}
        </div>
    );
}