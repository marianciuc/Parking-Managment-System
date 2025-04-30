import "@/components/sidebar/sidebar.css";
import {
    Icon as LucidIcon,
    PieChart,
    List,
    DollarSign,
    Star,
    Settings,
    Clock,
    LayoutPanelTop
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useContext, useEffect, useMemo, useState } from "react";
import { Context } from "@/main.tsx";
import { IParking } from "@/models/common";
import {ParkingSwitcher} from "@/components/parking-switcher.tsx";
// import {Button} from "@/components/ui/button"

function Sidebar() {
    const { parkingStore } = useContext(Context);
    const [parking, setParking] = useState<IParking | null>(null);
    const [parkingList, setParkingList] = useState<IParking[]>([]);
    const use_mock = import.meta.env.VITE_USE_MOCK as boolean;
    console.log("use_mock", use_mock);

    type Links = {
        name: string;
        img: typeof LucidIcon;
        linkTo: string;
    };

    const navigate = useNavigate();

    useEffect(() => {
        const fetchParking = async () => {
            try {
                await parkingStore.getActiveParking();
                setParking(parkingStore.parking);
            } catch (error) {
                console.error("Ошибка загрузки парковки:", error);
            }
        };
        // if (parkingStore.parkingList.length === 0)
        fetchParking();
        console.log("Call useEffect in Sidebar first")


        if (parkingStore.parking){
            setParking(parkingStore.parking)
        } else{
            console.log("Second call in useEffect")
            parkingStore.fetchParkingData().then(() => {
                setParkingList(parkingStore.parkingList);
            })
        }
        setParkingList(parkingStore.parkingList);
    }, [parkingStore.parking, parkingStore]);

    const handleParkingChange = (parking: IParking) => {
        parkingStore.setActiveParking(parking);
        navigate(`/parking/${parking.id}/dashboard`);
    }

    const links = useMemo(
        () => [
            { name: "Dashboard", img: PieChart, linkTo: `/dashboard` },
            { name: "Sessions", img: Clock, linkTo: `/sessions` },
            { name: "Parking Settings", img: Settings, linkTo: `/settings` },
            { name: "Access Lists", img: List, linkTo: `/access-list` },
            { name: "Tariffs", img: DollarSign, linkTo: `/tariffs` },
            { name: "Reviews", img: Star, linkTo: `/reviews` },
        ],
        []
    );

    return (
        <div className="sidebar flex flex-col items-start justify-between fixed left-0 h-screen top-0">
            <ParkingSwitcher
                parkingList={parkingList}
                activeParking={parking}
                setActiveParking={handleParkingChange}
                createParking={() => navigate("/parking/create")}>
            </ParkingSwitcher>

            {/* Ссылки */}
            <div className="sidebar-links w-full">
                {links.map((item: Links) => (
                    <button
                        key={item.name}
                        className={`sidebar-link ${
                            !parking ? "cursor-not-allowed opacity-50" : ""
                        }`}
                        onClick={() => {
                            if (parking)
                                navigate(`/parking/${parking.id}` + item.linkTo);
                        }}
                        disabled={!parking}
                    >
                        {/*//@ts-ignore*/}
                        <item.img className="icon" />
                        <span className="sidebar-text">{item.name}</span>
                    </button>
                ))}
            </div>

            {/* Нижний блок */}
            <div className="sidebar-footer hidden lg:block w-full">
                <button onClick={()=>{navigate("/")}} className="sidebar-link  w-full">
                    <LayoutPanelTop className="icon" />
                    <span className="sidebar-text">All parking's</span>
                </button>
            </div>
        </div>
    );
}

export default Sidebar;