import {IBankAccount, RecordStatus} from "@/models/common/IBankAccount.ts";
import BankCard from "@/components/cards/BankCard.tsx";
import {Button} from "@/components/ui/button.tsx";
import {useNavigate} from "react-router-dom";
import {Plus} from "lucide-react";


function YourPaymentData(){

    const mockBankAccount: IBankAccount[] =[ {
        id: '12345336',
        creationDate: [2024, 3, 5, 10, 30, 0],
        modificationDate: [2024, 3, 6, 12, 0, 0],
        recordStatus: RecordStatus.ACTIVE,
        iban: 'PL89370400440532013000',
        fullName: 'John Doe',
        bankName: 'SANTANDER',
        swiftCode: 'MOCKDEFFXXX',
        country: 'Germany'
    },
        {
            id: '123456',
            creationDate: [2024, 3, 5, 10, 30, 0],
            modificationDate: [2024, 3, 6, 12, 0, 0],
            recordStatus: RecordStatus.ACTIVE,
            iban: 'PL8937040044883033000',
            fullName: 'John Doe',
            bankName: 'BANK POLSKI',
            swiftCode: 'MOCKDEFFXXX',
            country: 'Germany'
        },
    ];

    const navigate = useNavigate();

    return (
        <div className="flex flex-col justify-start items-start w-full p-6 bg-white rounded-lg shadow gap-6">
            <div className="flex flex-col justify-start items-start ">
                <h3 className="block-title">Bank Accounts</h3>
                <p>Some information about bank accounts</p>
            </div>
            <Button className="self-end" variant="outline" onClick={() => {navigate('/profile/add-new-bank-account')}}>
                <Plus/> Add new
            </Button>
            <BankCard bankAccount={mockBankAccount[0]}/>
            <BankCard bankAccount={mockBankAccount[1]}/>
        </div>
    )

}
export default YourPaymentData;