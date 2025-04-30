import {IParking} from "@/models/common";
import ImageNameDescription from "@/components/blocks/ImageNameDescription.tsx";
import LocationInformation from "@/components/blocks/LocationInformation.tsx";
import ParkingSettingsDangerZone from "@/components/blocks/ParkingSettingsDangerZone.tsx";
import { motion } from "framer-motion";

function ParkingSettingsGeneralInformation ( {parking}:{parking :IParking}) {
    return (
        <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}>
            <ImageNameDescription parking={parking}/>
            <LocationInformation parking={parking}/>
            <ParkingSettingsDangerZone parking={parking}/>
        </motion.div>
    )
}

export default ParkingSettingsGeneralInformation;