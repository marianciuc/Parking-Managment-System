import {Button} from "@/components/ui/button"
import {Settings, Plus, ListMinus} from "lucide-react";
import {RequestPayoutDialog} from "@/components/dialogs/RequestPayoutDialog.tsx";


function FinancesActions(){


    // const {parkingId} = useParams<{ parkingId: string }>();


    // const {userStore} = useContext(Context);

    function handleButtton(){
        // const date = new Date(); // Current date
        // const toDate = new Date(); // Current date
        // date.setDate(date.getDate() - 7); // Subtract 7 days
        // console.log(date); // Output the new date
    }


    return (<div className="flex flex-col w-full p-6 bg-white items-start gap-6 rounded-md card">
        <h4 className="block-title">Actions</h4>
        <div className="flex flew-row justify-start items-center gap-3 flex-wrap">
            <Button variant="outline" className="flex-1" onClick={handleButtton}><Settings/>Change Currency</Button>
            <RequestPayoutDialog/>
            <Button variant="outline" className="flex-1"><Plus/>Top Up Balance</Button>
            <Button variant="outline" className="flex-1"><ListMinus/> See all transactions</Button>

        </div>
    </div>)
}
export default FinancesActions;
