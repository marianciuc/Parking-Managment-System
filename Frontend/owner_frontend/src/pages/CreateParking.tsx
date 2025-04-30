//parking components
import CreateParkingStep1 from "@/components/forms/CreateParkingStep1.tsx";

//modal
import EscapePage from "@/components/dialogs/EscapePage.tsx";


function CreateParking() {

    return (
        <div className="flex flex-col items-center justify-center relative mt-[15px] bg-white p-6 rounded-lg">
            <div className="flex flex-row mb-10 items-center justify-between mx-auto">
                <h1>Create new parking</h1>
                <div className="absolute right-10">
                    <EscapePage message={"You are leaving create Parking form"}/>
                </div>
            </div>
            <CreateParkingStep1/>
        </div>
    )
}

export default CreateParking;