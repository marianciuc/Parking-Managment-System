import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import {Send} from "lucide-react";
import {useContext, useState} from "react";
import {Context} from "@/main.tsx";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {RecordStatus} from "@/models/common/IBankAccount.ts";

const SRequestPayout = z.object({
    amount: z.number().nonnegative({
        message: "This field is required",
    }),
    bankAccount: z.string(),
})

const tariffs = [ {
    id: '123456',
    creationDate: [2024, 3, 5, 10, 30, 0], // Example: [Year, Month, Day, Hour, Minute, Second]
    modificationDate: [2024, 3, 6, 12, 0, 0],
    recordStatus: RecordStatus.ACTIVE,
    iban: 'PL89370400440532013000',
    fullName: 'John Doe',
    bankName: 'SANTANDER',
    swiftCode: 'MOCKDEFFXXX',
    country: 'Germany'
},
    {
        id: '12333456',
        creationDate: [2024, 3, 5, 10, 30, 0], // Example: [Year, Month, Day, Hour, Minute, Second]
        modificationDate: [2024, 3, 6, 12, 0, 0],
        recordStatus: RecordStatus.ACTIVE,
        iban: 'PL8937040044883033000',
        fullName: 'John Doe',
        bankName: 'BANK POLSKI',
        swiftCode: 'MOCKDEFFXXX',
        country: 'Germany'
    },
];

export function RequestPayoutDialog() {

    const {paymentsStore} = useContext(Context);
    const [open, setOpen] = useState(false);

    const form = useForm<z.infer<typeof  SRequestPayout >>({
        resolver: zodResolver(SRequestPayout),
        defaultValues: {
            amount: undefined,
            bankAccount: "",
        },
        mode: "onChange",
    });

    const handleSubmit = (values: z.infer< typeof SRequestPayout>) => {

        console.log("REquest");
        console.log("VAlues",values.amount);
        paymentsStore.requestPayout("abc", values.amount, values.bankAccount);
        setOpen(false);
        form.reset();
    }


    return (
        <Dialog open={open} onOpenChange={setOpen} >
            <DialogTrigger asChild>
                <Button variant="outline" className="flex-1"><Send />Request Payout</Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Request Payout</DialogTitle>
                    <DialogDescription>
                        Request Payout from your parking's here
                    </DialogDescription>
                </DialogHeader>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-6">
                        {/* Поле для имени тарифа */}
                        <FormField
                            control={form.control}
                            name="amount"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Amount</FormLabel>
                                    <FormControl>
                                        <Input
                                            type="number"
                                            step="0.01"
                                            min="0"
                                            placeholder="Enter amount"
                                            {...field}
                                            onChange={(e) => field.onChange(Number(e.target.value) || 0)}
                                            className="rounded-lg"
                                        />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name="bankAccount"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Bank Account</FormLabel>
                                    <FormControl>
                                        <Select onValueChange={field.onChange} defaultValue={field.value}>

                                            <SelectTrigger className="rounded-lg">
                                                <SelectValue placeholder="Select bank account"/>
                                            </SelectTrigger>

                                            <SelectContent>
                                                {tariffs.map((tariff) => (
                                                    tariff && tariff.id &&
                                                    <SelectItem key={tariff.id} value={tariff.bankName}>
                                                        {tariff.bankName}
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
                                Request
                            </Button>
                        </DialogFooter>
                    </form>
                </Form>


            </DialogContent>
        </Dialog>
    )
}
