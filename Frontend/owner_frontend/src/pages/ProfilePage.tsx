import {Tabs, TabsList, TabsTrigger, TabsContent} from "@/components/ui/tabs";
import { Lock, DollarSign, User} from "lucide-react";
import {useContext} from "react";
import PersonalInformation from "@/components/tabs/PersonalInformation.tsx";
import Security from "@/components/tabs/Security.tsx";
import {useNavigate} from "react-router-dom";
import {Context} from "@/main.tsx";
import PrettyDate from "@/components/ui/pretty-date.tsx";
import YourPaymentData from "@/components/tabs/YourPaymentData.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";

const tabs = ["personal", "security", "payments"];


function ProfilePage() {

    const navigate = useNavigate();

    const activeTab = tabs.find((tab) => location.pathname.includes(tab)) || "personal";

    const {userStore} = useContext(Context)

    const handleTabChange = (value: string) => {
        navigate(`/profile/${value}`);

    };

    return (
        <div className="min-h-screen bg-gray-100 text-gray-800 w-full max-w-[800px] ">
            <div className="">
                {/* Вкладки */}
                <Tabs defaultValue={activeTab} onValueChange={handleTabChange} className="">
                    <PageHeader title="Profile Settings" subtitle="Manage your profile settings and personal information." >
                    <TabsList className="flex justify-center gap-1">
                        <TabsTrigger
                            value="personal"
                        >
                            <User size={18} className="inline mr-2"/>
                            Personal Information
                        </TabsTrigger>
                        <TabsTrigger
                            value="security"
                        >
                            <Lock size={18} className="inline mr-2"/>
                            Security
                        </TabsTrigger>
                        <TabsTrigger
                            value="payments"
                        >
                            <DollarSign size={18} className="inline mr-2"/>
                            Your Payment Data
                        </TabsTrigger>
                    </TabsList>
                    </PageHeader>

                    {/* Содержимое вкладки: Персональные данные */}
                    <TabsContent value="personal" className="space-y-6">
                        <PersonalInformation/>
                    </TabsContent>
                    <TabsContent value="security" className="space-y-6">
                        <Security/>
                    </TabsContent>
                    <TabsContent value="payments" className="space-y-6">
                        <YourPaymentData/>
                    </TabsContent>
                </Tabs>
                <div className="flex flex-row justify-center gap-4 mt-6">

                    <div>Last update : <PrettyDate timestamp={userStore.user.modificationDate}/></div>
                    <div>Date of registration : <PrettyDate timestamp={userStore.user.creationDate}/></div>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;