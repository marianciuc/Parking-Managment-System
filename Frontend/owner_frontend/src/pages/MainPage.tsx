import Card from "@/components/cards/card.tsx";
import {PlusIcon} from "lucide-react"
import "@/pages/styles/mainpage.css"
import React, {useContext, useEffect, useMemo, useState} from "react";
import {Button} from "@/components/ui/button.tsx";
import {useNavigate} from "react-router-dom";
import SearchInput from "@/components/ui/SearchInput.tsx";
import SelectSortTypeShadcn from "@/components/selectSortType/SelectSortTypeShadcn.tsx";
import {Context} from "@/main.tsx";
import {IParking} from "@/models/common";
import {IAddress} from "@/models/common/IAddress.ts";
import PageHeader from "@/components/header/PageHeader.tsx";
import $api from "@/http";


type Comparator<T> = (a: T, b: T) => number;

function isIAddress(value: any): value is IAddress {
    return (
        value &&
        typeof value.id === "string" &&
        typeof value.countryCode === "string" &&
        typeof value.city === "string"
    )
}

function createComparator<T>(key: keyof T, order: 'asc' | 'desc' = 'asc'): Comparator<T> {
    return (a, b) => {
        const aValue = a[key];
        const bValue = b[key];

        if (typeof aValue === 'string' && typeof bValue === 'string') {
            return order === 'desc' ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
        }

        if (typeof aValue === 'number' && typeof bValue === 'number') {
            return order === 'asc' ? aValue - bValue : bValue - aValue;
        }
        if (typeof aValue === "boolean" && typeof bValue === 'boolean') {
            return order === 'desc' ? Number(bValue) - Number(aValue) : Number(aValue) - Number(bValue);
        }
        if (isIAddress(aValue) && isIAddress(bValue)) {
            return order === 'asc' ? aValue.city.localeCompare(bValue.city) : bValue.city.localeCompare(aValue.city);
        }

        if (aValue instanceof Object && bValue instanceof Object) {
            return 0; // Fallback for complex objects, customizable
        }

        return 0; // Equal if values are not comparable
    };
}

function MainPage() {
    const {parkingStore} = useContext(Context)
    const navigate = useNavigate();

    const [parkingDetails, setParkingDetails] = useState<IParking[]>([]);

    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        setLoading(true);
        console.log("loading start");
        try{
            parkingStore.fetchParkingData().then((data) => {
            if (data) {
                console.log("data: "  +data)
                setParkingDetails(data)
            }
            })
        }catch (e){
            console.log("error: " + e);
        }
        setLoading(false);
        console.log("loading stop");


    }, [parkingStore])


    const [selectedSort, setSelectedSort] = useState('isPinned');
    const [searchQuery, setSearchQuery] = useState('');

    const sortParkingList = (sort :string) => {
        setSelectedSort(sort);
    }


    const sortedParkingList = useMemo(() => {
        if (selectedSort) {
            console.log(selectedSort)
            const comparator = createComparator<IParking>(selectedSort as keyof IParking, 'asc');
            return [...parkingDetails].sort(comparator)
        } else return parkingDetails;
    }, [parkingDetails, selectedSort]);


    //search query for parking filter
    const sortedAndSearchParkingList = useMemo(() => {
        return sortedParkingList
            .filter(parking => parking.name.toLowerCase().includes(searchQuery.toLowerCase())
                // || parking.address.street.toLowerCase().includes(searchQuery.toLowerCase())
        );
    }, [searchQuery, sortedParkingList]);


    useEffect(() => {
        const fetchData = async () => {
            try{
                console.log("Fetch parking fetch" , "53.4525613, 14.5383154")
                const response = await $api.get(`/api/v1/parking/search`,{
                    params: {
                        page: 0,
                    },
                    data: {
                        latitude: 53.4525613,
                        longitude: 14.5383154,
                        radiusInMeters: 10000,
                    }


                })

                console.log("REsponse parking fetch" ,response)

                if(response.status  < 210){
                    // setParkingList(response.data)
                }



            } catch (error) {
                console.error("Failed to fetch parking:", error);
            }
        };

        fetchData();
    }, []);

    //pin/unpin logic
    // TODO: Connect with Backend
    const togglePin = (id: string) => {
        setParkingDetails(prevCards => {
            const updatedCards = prevCards.map(card =>
                card.id === id ? {...card, isPinned: !card.isPinned} : card
            );
            return updatedCards.sort((a, b) => Number(b.isPinned) - Number(a.isPinned));
        });
    };

    {console.log(sortedAndSearchParkingList)}


    return (
        <div className="w-full max-w-full h-full ">
            <PageHeader title="Welcome to Your Parking Management Hub! 🚗📋" subtitle="View all your parking locations in one place and manage them effortlessly. Quickly add new parking spaces, update details, and keep everything organized. Simple, fast, and efficient — your parking business starts here!"/>

            <div className="flex flex-col items-center justify-center w-full min p-6 ">
                <div className="main flex flex-col items-center justify-start mx-auto relative w-full gap-[20px] bg-white rounded-lg p-8">
                    <div className="search_create_main_page ">
                        <div>
                            <SearchInput
                                value={searchQuery}
                                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearchQuery(e.target.value)}

                                placeholder="Search for parking..."/>
                            <SelectSortTypeShadcn
                                value={selectedSort}
                                onChange={sortParkingList}
                                options={
                                    [
                                        {key: "isPinned", value: "Default Sort",},
                                        {key: "parkingStatus", value: "By status",},
                                        {key: "name", value: "By name",},
                                        {key: "capacity.capacity", value: "By slots",},
                                    ]
                                }
                                defaultOption="Sort"
                            />
                        </div>
                        <Button className="create_parking" onClick={() => (navigate("/parking/create"))}>
                            <PlusIcon size={20}/>
                            <span>Create new parking</span>
                        </Button>
                    </div>
                    <div className="grid_container w-full relative min-h-40">
                        {loading
                            ? <div className="absolute  top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">Loading...</div>
                            : sortedAndSearchParkingList.length === 0
                                ? <h2 className="absolute  top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">No parking to display</h2>
                                : sortedAndSearchParkingList.map((details: IParking) => (
                                    <Card className="grid_item"
                                        key={details.id}
                                        parking={details}
                                        togglePin={togglePin}
                                        onClick={() => {
                                          parkingStore.setActiveParking(details);
                                          navigate(`/parking/${details.id}/dashboard`)
                                        }
                                    }
                                    />
                                ))
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default MainPage;