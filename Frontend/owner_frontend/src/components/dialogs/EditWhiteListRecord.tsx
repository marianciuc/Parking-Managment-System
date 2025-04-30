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
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ITariffRecord, TariffClass } from "@/models/common/ITariffRecord";
import {SCreateWhiteListRecord} from "@/models/common/formSchemas";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {useParams} from "react-router-dom";
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import SmallLoader from "@/components/ui/small-loader.tsx";
import {IWhiteListRecord} from "@/models/common/dataTables";
import {toast} from "@/hooks/use-toast.ts";


function EditWhiteListContent({record, setOpen}: {record: IWhiteListRecord, setOpen: (open: boolean) => void}) {

    const form = useForm<z.infer<typeof SCreateWhiteListRecord>>({
        resolver: zodResolver(SCreateWhiteListRecord),
        defaultValues: {
            vehiclePlateNumber:record.vehiclePlate,
            tariff: record.tariffId,
        },
        mode: "onChange",
    });

    const {parkingId} = useParams<{ parkingId: string }>();


    const [tariffs, setTariffs] = useState<ITariffRecord[]>([]);

    const {tariffStore, accessManagementStore} = useContext(Context);

    const [loading, setLoading] = useState<boolean>(false);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }
                const toastMessage: IToastInfo | void = await tariffStore.fetchTariffData(parkingId);

                if (toastMessage) {
                    console.log(toastMessage);
                }
                setTariffs([...tariffStore.tariffs].filter((tariff) => tariff.tariffClass == TariffClass.FOR_WHITELISTED));
                console.log("Tariffs")
                console.log(tariffs);
            } catch (error) {
                console.error("Failed to fetch tariffs:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, tariffStore]);

    const handleSubmit = async (values: z.infer<typeof SCreateWhiteListRecord>) => {
        console.log(values)
        if(parkingId){
            const response = await accessManagementStore.updateWhitelistRecord(parkingId,values.vehiclePlateNumber,values.tariff);

            if(response){
                toast({
                    title: "Whitelist record",
                    description: response.message,
                    variant: response.type == ToastType.ERROR ? "destructive" : "default"
                })
            }
        }
        form.reset();
        setOpen(false);
    };

    const deleteRecord = async () => {
        if(parkingId){
            const response = await accessManagementStore.deleteWhitelistRecord(parkingId, record.id, record.vehiclePlate);

            if(response){
                toast({
                    title: "Whitelist record",
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
                    Edit whitelist record
                </DialogTitle>
                <DialogDescription className="text-sm text-gray-600">
                    Easily edit or delete whitelist record
                </DialogDescription>
            </DialogHeader>
            {loading ? <SmallLoader/> :
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
                                            className="rounded-lg" disabled={true}
                                        />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        {/* Выбор класса тарифа */}
                        <FormField
                            control={form.control}
                            name="tariff"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Class</FormLabel>
                                    <FormControl>
                                        <Select onValueChange={field.onChange} defaultValue={field.value}>

                                            <SelectTrigger className="rounded-lg">
                                                <SelectValue placeholder="Select tariff class"/>
                                            </SelectTrigger>

                                            <SelectContent>
                                                {tariffs.map((tariff) => (
                                                    tariff && tariff.id &&
                                                    <SelectItem key={tariff.id} value={tariff.id}>
                                                        {tariff.name}
                                                    </SelectItem>
                                                ))}
                                            </SelectContent>
                                        </Select>
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
                </Form>}

        </DialogContent>
    )
}


// Основной компонент
export default function
    EditWhiteListRecordDialog({record}:{record: IWhiteListRecord}){


    const [open, setOpen] = useState(false);




    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="ghost" className="h-8 w-8 p-0">
                    <span className="sr-only">Open menu</span>
                    <MoreHorizontal className="h-4 w-4" />
                </Button>
            </DialogTrigger>
            {open && <EditWhiteListContent record={record} setOpen={setOpen}/>}
        </Dialog>
    );
}