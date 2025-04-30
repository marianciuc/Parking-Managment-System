
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {MoreHorizontal} from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input"
import {SCreateBlackListRecord} from "@/models/common/formSchemas";
import {useContext, useState} from "react";
import {Context} from "@/main.tsx";
import {useParams} from "react-router-dom";
import {toast} from "@/hooks/use-toast.ts";
import {ToastType} from "@/models/common/IToastInfo.ts";
import {IBlackListRecord} from "@/models/common/dataTables";


function EditBlackListRecordContent({record, setOpen}:{record: IBlackListRecord, setOpen:(b: boolean)=>void}) {


    const form = useForm<z.infer<typeof SCreateBlackListRecord>>({
        resolver: zodResolver(SCreateBlackListRecord),
        defaultValues: {
            vehiclePlateNumber:record.plateNumber,
            reason: record.reason,
        },
        mode: "onChange",
    });

    const {parkingId} = useParams<{ parkingId: string }>();

    const {accessManagementStore} = useContext(Context);


    const handleSubmit = async (values: z.infer<typeof SCreateBlackListRecord>) => {
        console.log(values)
        if(parkingId){
            const response = await accessManagementStore.updateBlacklistRecord(parkingId,values.vehiclePlateNumber,values.reason);

            if(response){
                toast({
                    title: "Whitelist record",
                    description: response.message,
                    variant: "default"
                })
            }
        }
        form.reset();
        setOpen(false);
    };

    const deleteRecord = async () => {
        if(parkingId){
            const response = await accessManagementStore.deleteBlacklistRecord(parkingId, record.id, record.plateNumber);

            if(response){
                toast({
                    title: "Blacklist record",
                    description: response.message,
                    variant: response.type == ToastType.ERROR ? "destructive" : "default"
                })
            }
        }
        form.reset();
        setOpen(false);
    };

    return(
        <DialogContent className="sm:max-w-[500px]">
            {/* Заголовок модального окна */}
            <DialogHeader>
                <DialogTitle className="text-lg font-semibold text-gray-900">
                    Edit blacklist record
                </DialogTitle>
                <DialogDescription className="text-sm text-gray-600">
                    Easily edit or delete blacklist record
                </DialogDescription>
            </DialogHeader>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
                    {/* Поле для имени тарифа */}
                    <FormField
                        control={form.control}
                        name="vehiclePlateNumber"
                        render={({field}) => (
                            <FormItem>
                                <FormLabel>Name</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder="Enter vehicle plate number"
                                        {...field}
                                        disabled={true}
                                        className="rounded-lg"
                                    />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="reason"
                        render={({field}) => (
                            <FormItem>
                                <FormLabel>Reason</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder="Enter reason here"
                                        {...field}
                                        className="rounded-lg"
                                    />
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />

                    {/* Подвал с кнопкой отправки */}
                    <DialogFooter className="flex flex-row sm:justify-between w-full">
                        <Button type="button" variant="destructive" className="" onClick={deleteRecord}>
                            Delete Record
                        </Button>
                        <Button type="submit" variant="default" className="">
                            Save Changes
                        </Button>
                    </DialogFooter>
                </form>
            </Form>

        </DialogContent>
    )
}


// Основной компонент
export default function EditBlackListRecordDialog({record} : {record :IBlackListRecord}){


    const [open, setOpen] = useState<boolean>(false);


    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="ghost" className="h-8 w-8 p-0">
                    <span className="sr-only">Open menu</span>
                    <MoreHorizontal className="h-4 w-4" />
                </Button>
            </DialogTrigger>
            {open && <EditBlackListRecordContent record={record} setOpen={setOpen}/>}
        </Dialog>
    );
}