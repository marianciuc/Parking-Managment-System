import {IParking} from "@/models/common";
import ParkingDetailsCapacity from "@/components/blocks/ParkingDetailsCapacity.tsx";
import ParkingDetailsTags from "@/components/blocks/ParkingDetailsTags.tsx";
import { motion } from "framer-motion";

function ParkingSettingsDetails({parking}:{parking :IParking}) {
    return (
        <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}>
            <ParkingDetailsCapacity parking={parking}/>
            <ParkingDetailsTags parking={parking}/>
        </motion.div>
    )
}
export default ParkingSettingsDetails;