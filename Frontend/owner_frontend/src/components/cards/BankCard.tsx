import {
    DropdownMenu,
    DropdownMenuContent, DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import { MoreVertical} from "lucide-react";
import {IBankAccount} from "@/models/common/IBankAccount.ts";



function BankCard({bankAccount}:{bankAccount:IBankAccount}) {




    return (
        <div className="flex flex-row justify-between items-center gap-10 bg-gray-600 rounded-lg p-4 w-full">
            <div className="flex flex-col justify-start items-start w-full">
                <span className="text-gray-50 font-inter text-[20px] font-semibold leading-[28px] tracking-[-0.1px]">{bankAccount.bankName}</span>
                <span className="text-gray-300 font-inter text-[14px] font-normal leading-[20px]" >{bankAccount.iban}</span>
            </div>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <Button variant="ghost" className="h-8 w-8 p-0">
                        <span className="sr-only">Open menu</span>
                        <MoreVertical color="white" className="h-4 w-4" />
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                    <DropdownMenuLabel>Actions</DropdownMenuLabel>
                    <DropdownMenuItem
                        onClick={() => alert("not implemented")}
                    >
                        Delete
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onClick={() => alert("not implemented")}
                    >
                        Request Payout
                    </DropdownMenuItem>
                    <DropdownMenuItem
                        onClick={() => alert("not implemented")}
                    >
                        See all payouts
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
        </div>
    )

}
export default BankCard;