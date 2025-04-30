import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {IToastInfo} from "@/models/common/IToastInfo.ts";
import {IAccountBalance} from "@/models/common/IAccountBalance.ts";
import {Label} from "@/components/ui/label.tsx";
import SmallLoader from "@/components/ui/small-loader.tsx";


function BalanceBlockItem({message, balance, currency}:{message: string, balance: number, currency: string}) {
    return (
        <div className="flex flex-col text-left">
            <Label className="label">{message}</Label>
            <span className="text-2xl font-bold text-left mt-1">{balance} {currency}</span>
        </div>
    )
}


function BankAccountsBlock(){

    const {userStore, paymentsStore} = useContext(Context);

    const [balanceInfo, setBalanceInfo] = useState<IAccountBalance>({} as IAccountBalance);

    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!userStore.user.userId) {
                    console.warn("No user passed");
                    return;
                }

                console.log("Fetching user balance for userId:", userStore.user.userId);

                const toastMessage: IToastInfo | void = await paymentsStore.getAccountBalanceStats(userStore.user.userId);



                if (toastMessage) {
                    console.log("Toast Message:", toastMessage);
                }



                if (paymentsStore.accountBalanceStats) {
                    setBalanceInfo(paymentsStore.accountBalanceStats);
                }

            } catch (error) {
                console.error("Failed to fetch user balance:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [paymentsStore, userStore.user.userId]);




    return (<div className="flex flex-col p-6 bg-white items-start justify-start gap-6 rounded-md min-w-[300px] card">
        <div className="flex flex-col justify-start gap-2 text-left">
            <h4 className="block-title">Bank Accounts</h4>
            <span className="block-description">This section provides an overview of your account balance and financial activities.</span>
        </div>
        {loading ? <SmallLoader /> :<>
        <BalanceBlockItem message={"Your current balance"} balance={balanceInfo.currentBalance}  currency={balanceInfo.currency} />
        <BalanceBlockItem message={"Blocked amount"} balance={balanceInfo.blockedAmount} currency={balanceInfo.currency} />
        <BalanceBlockItem message={"Withdrawn amount"} balance={balanceInfo.withdrawnAmount} currency={balanceInfo.currency} />
       </> }</div>)
}
export default BankAccountsBlock;
