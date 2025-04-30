import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import {useNavigate} from "react-router-dom";
import FinancesActions from "@/components/blocks/FinancesActions.tsx";
import BankAccountsBlock from "@/components/blocks/BankAccountsBlock.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";
import {FinancesDataCharts} from "@/components/blocks/FinancesDataCharts.tsx";
import All_parkings from "@/components/plots/all_parkings.tsx";
import Payouts from "@/components/blocks/Payouts.tsx";
import Incoming from "@/components/blocks/Incoming.tsx";

const tabs = ["main", "payouts", "incoming", "parking"];


function Finances() {

    const navigate = useNavigate();

    const activeTab = tabs.find((tab) => location.pathname.includes(tab)) || "main";

    // const {userStore} = useContext(Context)

    const handleTabChange = (value: string) => {
        navigate(`/finances/${value}`);

    };


    return (
        <div className="w-full h-full">

            <Tabs defaultValue={activeTab} onValueChange={handleTabChange} className="w-full p-6">
                <PageHeader title="Finances" subtitle="Track parking revenue and expenses with charts showing earnings from different locations. Manage your finances easily and optimize your operations for better growth—all within the app.">
                    <TabsList>
                        <TabsTrigger value="main">Main</TabsTrigger>
                        <TabsTrigger value="payouts">Payouts</TabsTrigger>
                        {/*<TabsTrigger value="incoming">Incoming</TabsTrigger>*/}
                        <TabsTrigger value="parking">Parking</TabsTrigger>
                    </TabsList>
                </PageHeader>
                <div className="flex flex-row items-start justify-center gap-6 flex-nowrap relative w-full">
                    <div className="flex flex-col w-[300px] gap-6 mt-8">
                        <BankAccountsBlock/>
                        <FinancesActions/>
                    </div>


                    <TabsContent value="main" className="w-full">
                        <FinancesDataCharts/>
                    </TabsContent>
                    <TabsContent value="payouts" className="w-full">
                        <Payouts/>
                    </TabsContent>
                    {/*<TabsContent value="incoming" className="w-full"> </TabsContent>*/}
                    <TabsContent value="parking" className="w-full mt-8">
                        <div className="flex flex-col gap-4 w-full ">
                            <All_parkings/>
                            <div className="card">
                                <Incoming/>
                            </div>
                        </div>
                    </TabsContent>
                </div>
            </Tabs>

        </div>
    )
}

export default Finances;
