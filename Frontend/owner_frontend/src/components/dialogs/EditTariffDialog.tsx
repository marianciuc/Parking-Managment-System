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
import { SCreateTariff } from "@/models/common/formSchemas";
import {useContext, useState} from "react";
import {toast} from "@/hooks/use-toast.ts";
import {ToastType} from "@/models/common/IToastInfo.ts";
import {Context} from "@/main.tsx";
import {useParams} from "react-router-dom";

// Тип для свойств компонента
interface EditTariffDialogProps {
    tariffRecord: ITariffRecord;
}

// Основной компонент
export function EditTariffDialog({ tariffRecord }: EditTariffDialogProps) {
    // Инициализация формы с использованием Zod для валидации
    const form = useForm<z.infer<typeof SCreateTariff>>({
        resolver: zodResolver(SCreateTariff),
        defaultValues: {
            name: tariffRecord.name,
            description: tariffRecord.description,
            class: tariffRecord.tariffClass,
            price: tariffRecord.price,
        },
        mode: "onChange",
    });

    const {parkingId} = useParams<{ parkingId: string }>();

    const [open, setOpen] = useState(false);

    const {tariffStore} = useContext(Context)

    // Обработка отправки формы
    const handleSubmit = async (values: z.infer<typeof SCreateTariff>) => {

        if(tariffRecord.id && parkingId){
            const newTariff: ITariffRecord = {
                id: null,
                creationDate: null,
                modificationDate: null,
                name: values.name,
                tariffClass: values.class as TariffClass,
                description: values.description,
                price: Number(values.price),
                recordStatus:null,
                parkingId: null,
                currency: null,
                version:null
            };
            const response = await tariffStore.updateTariff(newTariff, tariffRecord.id, parkingId )
            if(response){
                toast({
                    title: "Tariff update",
                    description: response.message,
                    variant: response.type == ToastType.ERROR ? "destructive" : "default"
                })
            }

        }

        setOpen(false);
    };

    const deleteTariff = async () => {
        if(parkingId && tariffRecord.id){
            const response = await tariffStore.deleteTariff(tariffRecord.id, parkingId);
            if(response){
                toast({
                    title: "Tariff delete",
                    description: response.message,
                    variant: response.type == ToastType.ERROR ? "destructive" : "default"
                })
            }
        }
        setOpen(false);
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="ghost" className="h-8 w-8 p-0">
                    <span className="sr-only">Open menu</span>
                    <MoreHorizontal className="h-4 w-4" />
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
                {/* Заголовок модального окна */}
                <DialogHeader>
                    <DialogTitle className="text-lg font-semibold text-gray-900">Edit Tariff</DialogTitle>
                    <DialogDescription className="text-sm text-gray-600">
                        Use this form to edit or delete existing tariff in your system.
                    </DialogDescription>
                </DialogHeader>

                {/* Форма создания тарифа */}
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
                        {/* Поле для имени тарифа */}
                        <FormField
                            control={form.control}
                            name="name"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Name</FormLabel>
                                    <FormControl>
                                        <Input
                                            placeholder="Enter tariff name"
                                            {...field}
                                            className="rounded-lg"
                                        />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Поле для описания тарифа */}
                        <FormField
                            control={form.control}
                            name="description"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Description</FormLabel>
                                    <FormControl>
                                        <Input
                                            placeholder="Enter tariff description"
                                            {...field}
                                            className="rounded-lg"
                                        />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Выбор класса тарифа */}
                        <FormField
                            control={form.control}
                            name="class"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Class</FormLabel>
                                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                                        <FormControl>
                                            <SelectTrigger className="rounded-lg">
                                                <SelectValue placeholder="Select tariff class" />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {Object.values(TariffClass).map((tariffClass) => (
                                                <SelectItem key={tariffClass} value={tariffClass}>
                                                    {tariffClass}
                                                </SelectItem>
                                            ))}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Поле для цены тарифа */}
                        <FormField
                            control={form.control}
                            name="price"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel>Price</FormLabel>
                                    <FormControl>
                                        <Input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            placeholder="Enter tariff price"
                                            {...field}
                                            onChange={(e) => field.onChange(Number(e.target.value) || 0)}
                                            className="rounded-lg"
                                        />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Подвал с кнопкой отправки */}
                        <DialogFooter className="flex flex-row sm:justify-between w-full">
                            <Button type="button" variant="destructive" className="" onClick={deleteTariff}>
                               Delete Tariff
                            </Button>
                            <Button type="submit" variant="default" className="">
                                Save Changes
                            </Button>
                        </DialogFooter>
                    </form>
                </Form>
            </DialogContent>
        </Dialog>
    );
}