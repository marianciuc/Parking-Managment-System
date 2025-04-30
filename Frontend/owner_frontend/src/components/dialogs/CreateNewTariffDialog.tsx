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
import { SCreateTariff } from "@/models/common/formSchemas";
import {useState} from "react";

// Тип для свойств компонента
interface CreateNewTariffDialogProps {
    onAddTariff?: (tariff: ITariffRecord) => void;
}

// Основной компонент
export function CreateNewTariffDialog({ onAddTariff }: CreateNewTariffDialogProps) {
    // Инициализация формы с использованием Zod для валидации
    const form = useForm<z.infer<typeof SCreateTariff>>({
        resolver: zodResolver(SCreateTariff),
        defaultValues: {
            name: "",
            description: "",
            class: "",
            price: 0,
        },
        mode: "onChange",
    });



    const [open, setOpen] = useState(false);

    // Обработка отправки формы
    const handleSubmit = (values: z.infer<typeof SCreateTariff>) => {


        if (onAddTariff) {
            console.log("onAddTariff", onAddTariff);
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
            onAddTariff(newTariff);
        }
        setOpen(false);
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="outline" className="flex items-center gap-2 bg-black text-white">
                    <Plus size="16" />
                    Create
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[500px]">
                {/* Заголовок модального окна */}
                <DialogHeader>
                    <DialogTitle className="text-lg font-semibold text-gray-900">Create New Tariff</DialogTitle>
                    <DialogDescription className="text-sm text-gray-600">
                        Use this form to create a new tariff for your system.
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
                        <DialogFooter>
                            <Button type="submit" variant="default" className="w-full">
                                Create Tariff
                            </Button>
                        </DialogFooter>
                    </form>
                </Form>
            </DialogContent>
        </Dialog>
    );
}