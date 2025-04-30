
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Plus } from "lucide-react";
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
import {IPage} from "@/models/common";


// Основной компонент
export default function CreateBlackListRecordDialog({setBlackList} : {setBlackList: (page:IPage<IBlackListRecord>)=>void}){


    const [open, setOpen] = useState(false);


    const form = useForm<z.infer<typeof SCreateBlackListRecord>>({
        resolver: zodResolver(SCreateBlackListRecord),
        defaultValues: {
            vehiclePlateNumber:"",
            reason: "",
        },
        mode: "onChange",
    });

    const {parkingId} = useParams<{ parkingId: string }>();

    const {accessManagementStore} = useContext(Context);


    const handleSubmit = async (values: z.infer<typeof SCreateBlackListRecord>) => {
        console.log(values)
        if(parkingId)
             await accessManagementStore.addBlacklistRecord(parkingId,values.vehiclePlateNumber,values.reason).then(
                (toastMessage) => {
            console.log(toastMessage);
            if (toastMessage) {
                toast({
                    title: "Black List creation",
                    description: toastMessage.message,
                    variant: toastMessage.type == ToastType.ERROR ? "destructive" : "default"
                })
            }
            console.log(accessManagementStore.page);
            console.log(accessManagementStore.pageUpdate);
            setBlackList(accessManagementStore.page as IPage<IBlackListRecord>);
        }
            );
        form.reset();
        setOpen(false);
    };


    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="outline" className="flex items-center gap-2 bg-black text-white">
                    <Plus size="16"/>
                    Add
                </Button>


            </DialogTrigger>
            {open && <DialogContent className="sm:max-w-[500px]">
                {/* Заголовок модального окна */}
                <DialogHeader>
                    <DialogTitle className="text-lg font-semibold text-gray-900">
                        Create blacklist record
                    </DialogTitle>
                    <DialogDescription className="text-sm text-gray-600">
                        Easily deploy new blacklist record
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
                            <DialogFooter>
                                <Button type="submit" variant="default" className="w-full">
                                    Add
                                </Button>
                            </DialogFooter>
                        </form>
                    </Form>

            </DialogContent>}
        </Dialog>
    );
}