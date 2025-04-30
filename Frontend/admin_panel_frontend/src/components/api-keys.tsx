import {Context} from "@/providers/ClientContextProvider";
import React, {useContext, useEffect, useState} from "react";
import {APIKey, APIKeyListElement} from "@/interfaces";
import {toast} from "sonner";
import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {Button} from "@/components/ui/button";
import {
    Dialog, DialogClose,
    DialogContent,
    DialogDescription, DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Copy} from "lucide-react";

export default function ApiKeys({id}: {id: string}) {
    const {apiKeysStore} = useContext(Context)

    const [apiKeys, setApiKeys] = useState<APIKeyListElement[]>([])
    const [page, setPage] = useState<number>(1)
    const [size, setSize] = useState<number>(10)
    const [total, setTotal] = useState<number>(0)
    const [status, setStatus] = useState<string>("ACTIVE")
    const [loading, setLoading] = useState<boolean>(false)
    const [currentKey, setCurrentKey] = useState<APIKey | null>(null)

    const fetchApiKeys = async () => {
        setLoading(true)
        apiKeysStore.fetchKeys({
            page: page - 1, size, id: "", status, parkingId: id
        }).then(res => {
            if (res) {
                toast.error("Error!", {description: res.message})
            } else {
                setApiKeys(apiKeysStore.apiKeysList);
            }
            setLoading(false)
        })
    }

    const handleOpenKey = (keyId: string) => {
        setLoading(true);
        apiKeysStore.fetchApiKeyDetails(keyId).then((res) => {
            if (res) {
                toast.error("Error!", {description: res.message})
            } else {
                setCurrentKey(apiKeysStore.apiKeyDetails);
            }
            setLoading(false);
        })
    }

    const handleCreateApiKey = () => {
        apiKeysStore.generateKey(id).then(res => {
            if (res.messageType == "success") {
                toast.success("Success!", {description: res.message})
            } else {
                toast.error("Error!", {description: res.message})
            }
        })
    }

    useEffect(() => {
        fetchApiKeys()
    }, [])

    useEffect(() => {
        setApiKeys(apiKeysStore.apiKeysList)
    }, [apiKeysStore.apiKeysList]);

    return (
        <div className="flex flex-col gap-6 pt-10">
            <Button onClick={() => {handleCreateApiKey()}}>Create API Key</Button>

            <Table className="w-full mt-6">
                <TableCaption>A list of your recent invoices.</TableCaption>
                <TableHeader>
                    <TableRow>
                        <TableHead className="w-[200px]">ID</TableHead>
                        <TableHead>Status</TableHead>
                        <TableHead>Parking ID</TableHead>
                        <TableHead>Scope</TableHead>
                        <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                </TableHeader>
                {apiKeys && <TableBody>
                    {apiKeys.map((apiKey: APIKeyListElement) => (
                        <TableRow key={apiKey.id}>
                            <TableCell className="font-medium">{apiKey.id}</TableCell>
                            <TableCell>{apiKey.status}</TableCell>
                            <TableCell>{apiKey.parkingId}</TableCell>
                            <TableCell>{apiKey.scope}</TableCell>
                            <TableCell className="text-right space-x-2">
                                <Button>Revoke</Button>
                                <Dialog>
                                    <DialogTrigger><Button onClick={() => handleOpenKey(apiKey.id)}>See Key</Button></DialogTrigger>
                                    <DialogContent>
                                        <DialogHeader>
                                            <DialogTitle>Are you absolutely sure?</DialogTitle>
                                            <DialogDescription>
                                                This action cannot be undone. This will permanently delete your account
                                                and remove your data from our servers.
                                            </DialogDescription>
                                        </DialogHeader>
                                        {currentKey && <div className="flex items-center space-x-2">
                                            <div className="grid flex-1 gap-2">
                                                <Label htmlFor="link" className="sr-only">
                                                    Link
                                                </Label>
                                                <Input
                                                    id="link"
                                                    defaultValue={currentKey.key}
                                                    readOnly
                                                />
                                            </div>
                                            <Button type="submit" size="sm" className="px-3">
                                                <span className="sr-only">Copy</span>
                                                <Copy/>
                                            </Button>
                                        </div>}
                                            <DialogFooter className="sm:justify-start">
                                                <DialogClose asChild>
                                                    <Button type="button" variant="secondary">
                                                        Close
                                                    </Button>
                                                </DialogClose>
                                            </DialogFooter>
                                    </DialogContent>
                                </Dialog>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>}
            </Table>
        </div>
    )
}