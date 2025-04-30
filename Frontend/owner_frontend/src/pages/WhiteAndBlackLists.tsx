import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import {useNavigate, useParams} from "react-router-dom";
import WhiteList from "@/components/tabs/WhiteList.tsx";
import BlackList from "@/components/tabs/BlackList.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";


const tabs = ["whitelist", "blacklist"];

function WhiteAndBlackLists(){
    const {parkingId} = useParams();


    const navigate = useNavigate();


    const activeTab = tabs.find((tab) => location.pathname.includes(tab)) || "whitelist";


    console.log(activeTab);

    const handleTabChange = (value: string) => {
        if (parkingId) {
            navigate(`/parking/${parkingId}/access-list/${value}`);
        } else {
            console.error("Parking ID is missing.");
        }
    };

    return (<div className="w-full h-full m-8">

        <Tabs  defaultValue={activeTab} onValueChange={handleTabChange} className="w-full">
            <PageHeader title="Access Management" subtitle="Take total control of who enters your parking facility with the Access Management page. Whether you're rolling out the red carpet for VIPs or keeping unwanted vehicles out, the power is in your hands with customizable White Lists and Black Lists.">
                <TabsList>
                    <TabsTrigger value="whitelist">White list</TabsTrigger>
                    <TabsTrigger value="blacklist">Black list</TabsTrigger>
                </TabsList>
            </PageHeader>

            <TabsContent value="whitelist" className="w-full"><WhiteList/></TabsContent>
            <TabsContent value="blacklist"><BlackList/></TabsContent>
        </Tabs>


    </div>);
}
export default WhiteAndBlackLists;