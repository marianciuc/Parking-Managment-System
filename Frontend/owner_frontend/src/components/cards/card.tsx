import '@/components/cards/card.css'
import {Star} from "lucide-react"

import {IParking} from "@/models/common";
import {ParkingStatus} from "@/models/common/IParking.ts";


function Card({
                  className = "",
                  parking,
    //@ts-ignore
                  togglePin,
                  onClick
              }: {
    className?: string,
    parking: IParking,
    togglePin: (parkingName: string) => void
    onClick?: () => void
}) {
    return (
        <button onClick={onClick} className={`card_custom ${parking.parkingStatus == ParkingStatus.OPEN ? "open_parking_background"
            : parking.parkingStatus === ParkingStatus.CLOSED ? "close_parking_background" : "creation_process_background"
        } ${className}`}>
            <div className="top-block">
                <div>
                    <h3>{parking.name}</h3>
                    <h4>{parking.address.countryCode}, {parking.address.city}, {parking.address.street}, {parking.address.buildingNumber}</h4>
                </div>
                <div className="favorite"><Star color="white"/></div>
            </div>
            <div className="down-block">
                <div className="capacity-block">
                    <span>Slots occupied</span>
                    <span className="capacity-info">{parking.capacity.occupiedSpaces}/{parking.capacity.capacity}</span>
                </div>
                <span className="status">
                    {parking.parkingStatus === "OPEN" && <span className="text-white">OPEN</span>}
                    {parking.parkingStatus === "CREATION_PROCESS" &&
                        <span className="text-white">CREATION</span>}
                    {parking.parkingStatus === "CLOSED" && <span className="text-white"> CLOSED</span>}
                    {parking.parkingStatus === "TEMPORARY_CLOSED" && <span className="text-white"> TEMPORARY CLOSE</span>}
                </span>
            </div>
        </button>
    )
}

export default Card;
