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
import {toast} from "@/hooks/use-toast.ts";


function CreateWhiteListContent({setOpen}:{setOpen: (open : boolean) => void}) {


    const form = useForm<z.infer<typeof SCreateWhiteListRecord>>({
        resolver: zodResolver(SCreateWhiteListRecord),
        defaultValues: {
            vehiclePlateNumber:"",
            tariff: "",
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
            const response = await accessManagementStore.addWhitelistRecord(parkingId,values.vehiclePlateNumber,values.tariff);
            if(response){
                toast({
                    title: "Whitelist record creation",
                    description: response.message,
                    variant: response.type == ToastType.ERROR ? "destructive" : "default"
                })
            }
        }
        form.reset();
        setOpen(false);
    };



    return (<DialogContent className="sm:max-w-[500px]">
        {/* Заголовок модального окна */}
        <DialogHeader>
            <DialogTitle className="text-lg font-semibold text-gray-900">
                Create whitelist record
            </DialogTitle>
            <DialogDescription className="text-sm text-gray-600">
                Easily deploy new whitelist record
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
                                        className="rounded-lg"
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
                    <DialogFooter>
                        <Button type="submit" variant="default" className="w-full">
                            Add
                        </Button>
                    </DialogFooter>
                </form>
            </Form>}

    </DialogContent>)
}

// Основной компонент
export default function CreateWhiteListRecordDialog(){

    
    const [open, setOpen] = useState(false);

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="outline" className="flex items-center gap-2 bg-black text-white">
                    <Plus size="16"/>
                    Add
                </Button>


            </DialogTrigger>
            {open && <CreateWhiteListContent setOpen={setOpen}/> }
        </Dialog>
    );
}