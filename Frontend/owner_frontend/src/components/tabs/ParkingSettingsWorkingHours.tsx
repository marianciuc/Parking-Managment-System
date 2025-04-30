import {IParking} from "@/models/common";
import WorkingHours from "@/components/blocks/WorkingHours.tsx";
import { motion } from "framer-motion";

function ParkingSettingsWorkingHours ({parking}: {parking :IParking}) {

    return (
        <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}>
           <WorkingHours parking={parking}/>
        </motion.div>
    )
}

export default ParkingSettingsWorkingHours;