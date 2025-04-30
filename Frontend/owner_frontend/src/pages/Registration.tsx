import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs.tsx";
import RegistrationFormStep2 from "@/components/forms/RegistrationFormStep2.tsx";
import { useState } from "react";
import '@/pages/styles/registration.css';
import RegistrationFormStep3 from "@/components/forms/RegistrationFormStep3.tsx";

function Registration( ) {
    const [currentTab, setCurrentTab] = useState("step1");
    const [isStep2Enabled, setIsStep2Enabled] = useState(false);

    const handleNextStep = () => {
        setIsStep2Enabled(true); // Enable Step 2
        setCurrentTab("step2"); // CustomSwitch to Step 2
    };


    const handleValueChange = (value: string) => {
        // Prevent switching to disabled tabs
        if (value === "step2" && !isStep2Enabled) {
            return;
        }
        setCurrentTab(value);
    };

    return (
        <div className="flex flex-col items-center justify-center w-full h-full max-w-screen gap-20">
            <img src="/logo_text_blue.svg" alt="Logo Image" />
            <Tabs value={currentTab} onValueChange={handleValueChange} className="w-[600px] gap-5">
                <TabsList className="grid w-full grid-cols-2 gap-2">
                    <TabsTrigger
                        value="step1"
                        className={!isStep2Enabled ? "" : "disabled-tab"}
                    >
                        Step 1 ( Personal Information )
                    </TabsTrigger>
                    <TabsTrigger
                        value="step2"
                        className={isStep2Enabled ? "" : "disabled-tab"}
                    >
                        Step 2 ( Address )
                    </TabsTrigger>
                </TabsList>
                <TabsContent value="step1" className="mt-5">
                    <RegistrationFormStep2 onNextStep={handleNextStep} />
                </TabsContent>
                <TabsContent value="step2" className="mt-5">
                    <RegistrationFormStep3/>
                </TabsContent>
            </Tabs>
        </div>
    );
}

export default Registration;
