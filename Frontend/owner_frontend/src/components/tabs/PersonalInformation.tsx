import PersonalInformationForm from "@/components/blocks/PersonalInformationForm.tsx";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Camera} from "lucide-react";
import {useState} from "react";
import PersonalAddressForm from "@/components/blocks/PersonalAddressForm.tsx";


function PersonalInformation(){
    const [avatar] = useState("https://via.placeholder.com/150");

    return (
        <div className="flex flex-col gap-10">
        <div className="bg-white shadow rounded-lg p-6 space-y-8">
            {/* Аватар */}
            <div className="flex items-center space-x-8">
                <Avatar className="w-28 h-28 shadow-xl">
                    <AvatarImage src={avatar} alt="User Avatar"/>
                    <AvatarFallback className="text-2xl bg-gray-300 text-gray-700">
                        Н
                    </AvatarFallback>
                </Avatar>
                <div className="flex flex-col items-center space-x-8 justify-center">
                    <h2 className="text-xl font-bold text-gray-800">Profile</h2>
                    <p className="text-sm text-gray-500">
                        Update your personal information.
                    </p>
                    <Button
                        size="sm"
                        variant="outline"
                        className="mt-3 flex items-center gap-2 content-center"
                    >
                        <Camera size={16}/>
                        Change avatar
                    </Button>
                </div>
            </div>

            <PersonalInformationForm/>
        </div>
            <div className="bg-white shadow rounded-lg p-6 space-y-8">
                <h1>Address</h1>
                <PersonalAddressForm/>
            </div>
        </div>
    )
}
export default PersonalInformation ;