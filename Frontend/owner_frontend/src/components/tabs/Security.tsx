import ChangePasswordForm from "@/components/forms/ChangePasswordForm.tsx";
import {Button} from "@/components/ui/button"
import {Badge} from "@/components/ui/badge.tsx";
import {useNavigate} from "react-router-dom";
import {useContext} from "react";
import {Context} from "@/main.tsx";

function Security(){

    const navigate = useNavigate();

    const {userStore} = useContext(Context);

    //Profile verification is checked by isEmailVerified, need to change

    return (
        <div className="flex flex-col justify-start items-center gap-10">
            <div className="flex flex-col justify-start items-start w-full p-6 bg-white rounded-lg shadow gap-9">
                <div className="flex flex-col justify-start items-start gap-2">
                    <h4 className="block-title">Change password</h4>
                    <p className="block-description">
                        Some information about changing password
                    </p>
                </div>
                <ChangePasswordForm />
            </div>

            <div className="flex flex-col justify-start items-start w-full p-6 bg-white rounded-lg shadow gap-1">
                <div className="flex flex-row justify-between items-center gap-10 w-full">
                    <h4 className="block-title">Account Verification</h4>
                    {userStore.user.isEmailVerified? <Badge variant="secondary" className=" text-green-400"> Profile verified</Badge>
                        :<Badge variant="secondary" className=" text-red-400"> Unverified profile</Badge>}
                </div>
                <p className="block-description">Some information about account verification</p>
                <div className="flex w-full flex-row justify-end">
                   <Button onClick={()=>navigate("/profile/verification")}>
                       Verify
                   </Button>
                </div>
            </div>
        </div>
    )
}
export default Security;