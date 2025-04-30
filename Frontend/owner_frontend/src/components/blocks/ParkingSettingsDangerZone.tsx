import {IParking} from "@/models/common";
import {
    Dialog,
    DialogContent,
    DialogDescription, DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import {Button} from "@/components/ui/button";
import {Context} from "@/main.tsx";
import {useContext} from "react";
import {useParams, useNavigate} from "react-router-dom";


//TODO : modal windows close / delete

function ParkingSettingsDangerZone({parking}:{parking: IParking}) {

    const {parkingStore} = useContext(Context);
    const {parkingId} = useParams<{ parkingId: string }>();

    const navigate = useNavigate();

    console.log(parking);


    async function closeTemporary() {
        console.log("closeTemporary");

        if (!parkingId) return;

        try {
            await parkingStore.temporaryClose(parkingId);
        } catch (e) {
            console.error("Error closing temporarily:", e);
        }
    }

    async function deleteParking() {
        console.log("deleteParking");

        if (!parkingId) return;

        try {
            await parkingStore.deleteParking(parkingId); // Await if it's async
            navigate("/");
        } catch (e) {
            console.error("Error deleting parking:", e);
        }
    }

    return (
        <div>
            <div className="flex flex-col justify-start items-start text-left gap-5 border-b-2 p-2 label-text">
                <h3>Danger zone</h3>
                <p> In this block you control the condition of the parking lot, proceed with caution</p>
            </div>
            <div className="flex flex-col justify-start items-start text-left  border-red-100 border-2 rounded-md mt-5">
                <div className="border-b-2 border-red-100 w-full flex flex-row items-center justify-between">
                    <div className="text-left p-5 w-3/4 text-wrap">
                        <h4>Temporary close</h4>
                        <p> Your parking lot is now in operation, you can temporarily lock access to it and later unlock
                            it immediately</p>
                    </div>
                    <div className="flex justify-center items-center w-1/4 ">
                        <Dialog>
                            <DialogTrigger className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0 shadow-sm hover:bg-destructive/90 h-9 px-4 py-2 bg-rose-600 text-white">Close</DialogTrigger>
                            <DialogContent>
                                <DialogHeader>
                                    <DialogTitle>Temporarily Close Parking</DialogTitle>
                                    <DialogDescription>
                                        Are you sure you want to temporarily close this parking area? Users will not be able to park here until it is reopened.
                                    </DialogDescription>
                                </DialogHeader>
                                <DialogFooter>
                                    <Button  type="button" onClick={closeTemporary}>Confirm</Button>
                                </DialogFooter>
                            </DialogContent>
                        </Dialog>
                    </div>

                </div>
                <div className="border-b-2 border-red-100 w-full flex flex-row items-center justify-between">
                    <div className="text-wrap w-3/4 p-5 ">
                        <h4>Delete parking</h4>
                        <p>The procedure for deleting a parking lot is final and you will not be able to restore it, but
                            it will be archived</p>
                    </div>
                    <div className="flex justify-center items-center  w-1/4">
                        <Dialog>
                            <DialogTrigger className="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0 shadow-sm hover:bg-destructive/90 h-9 px-4 py-2 bg-rose-600 text-white">Delete</DialogTrigger>
                            <DialogContent>
                                <DialogHeader>
                                    <DialogTitle>Delete Parking</DialogTitle>
                                    <DialogDescription>
                                        Are you sure you want to permanently delete this parking area? This action cannot be undone.
                                    </DialogDescription>
                                </DialogHeader>
                                <DialogFooter>
                                    <Button type="submit" variant="destructive" className={"bg-rose-600"} onClick={deleteParking}>Delete parking</Button>
                                </DialogFooter>
                            </DialogContent>
                        </Dialog>
                    </div>


                </div>
            </div>
        </div>
    )
}

export default ParkingSettingsDangerZone;