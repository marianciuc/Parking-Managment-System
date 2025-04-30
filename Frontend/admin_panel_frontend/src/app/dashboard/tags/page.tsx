"use client";

import React, {useContext, useEffect, useState} from "react";
import { Button } from "@/components/ui/button";
import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import { Select, SelectTrigger, SelectContent, SelectGroup, SelectItem } from "@radix-ui/react-select";
import * as XLSX from "xlsx";
import jsPDF from "jspdf";
import {ParkingListItem, TagDomain} from "@/interfaces";
import Link from "next/link";
import {Context} from "@/providers/ClientContextProvider";
import {toast} from "sonner";
import {
    Dialog,
    DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";

export default function TagsPage() {
    const {tagStore} = useContext(Context);

    const [tags, setTags] = useState<TagDomain[]>([]);
    const [loading, setLoading] = useState(false);
    const [currentTag, setCurrentTag] = useState<TagDomain | null>(null);
    const [name, setName] = useState("");
    const [tagId, setTagId] = useState("");

    const [createName, setCreateName] = useState("");
    const [createDescription, setCreateDescription] = useState("");

    useEffect(() => {
        fetchTags();
    }, [name, tagId])

    const fetchTags = async () => {
        tagStore.searchTags({
            name: "",
            tagId: ""
        }).then(res => {
            if (res) {
                toast.error("Error!", {description: res.message});
            } else {
                setTags(tagStore.getTags);
            }
            setLoading(false);
        })
    }

    const handleCreateTag = async () => {
        if (createName.trim() == "" || createDescription.trim() == "") {
            toast.error("Error!", {description: "Name and description are required."});
            return;
        }
        tagStore.createTag({
            name: createName,
            description: createDescription
        }).then(res => {
            if (res.messageType == "success") {
                toast.success("Success!", {description: res.message});
                setCreateName("");
                setCreateDescription("");
                fetchTags();
            } else {
                toast.error("Error!", {description: res.message});
            }
        })
    }

    return (
        <div className="p-4">
            <h1>Tags Management</h1>

            <div className="card p-4 mt-5">

                <Dialog>
                    <DialogTrigger asChild>
                        <Button>Add Tag</Button>
                    </DialogTrigger>
                    <DialogContent className="sm:max-w-md">
                        <DialogHeader>
                            <DialogTitle>Create new Parking Tag</DialogTitle>
                            <DialogDescription>
                                Create a new parking tag to be used in your parking management.
                            </DialogDescription>
                        </DialogHeader>
                        <div className="flex flex-col gap-4">
                            <Label htmlFor="name" className="sr-only">
                                Name
                            </Label>
                            <Input id="name" placeholder="Name" value={createName} onChange={(e) => {setCreateName(e.target.value)}}/>
                            <Label htmlFor="description" className="sr-only">
                                Description
                            </Label>
                            <Input id="description" placeholder="Description" value={createDescription} onChange={(e) => {setCreateDescription(e.target.value)}}/>
                        </div>
                        <DialogFooter className="sm:justify-start">
                            <Button type="submit" size="sm" className="px-3 mt-3" onClick={() => {handleCreateTag()}}>
                                Create
                            </Button>
                            {/*<DialogClose asChild>*/}
                            {/*    <Button type="button" variant="secondary">*/}
                            {/*        Close*/}
                            {/*    </Button>*/}
                            {/*</DialogClose>*/}
                        </DialogFooter>
                    </DialogContent>
                </Dialog>
                <Table className="container mt-5">
                    <TableCaption>A list of your recent invoices.</TableCaption>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[200px]">Name</TableHead>
                            <TableHead>Description</TableHead>
                            <TableHead>Created At</TableHead>
                            <TableHead>Updated At</TableHead>
                            <TableHead>Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    {tags && <TableBody>
                        {tags.map((tag: TagDomain) => (
                            <TableRow key={tag.id}>
                                <TableCell className="font-medium">{tag.name}</TableCell>
                                <TableCell>{tag.description}</TableCell>
                                <TableCell>{tag.createdAt}</TableCell>
                                <TableCell>{tag.updatedAt}</TableCell>
                                <TableCell><Button>Delete</Button> <Button>Update</Button></TableCell>
                            </TableRow>
                        ))}
                    </TableBody>}
                </Table>

            </div>
        </div>
    );
}