import {IAddress, IOpeningHours, IParking} from "@/models/common";
import Tag from "@/components/ui/tag.tsx";
import {SetStateAction, useEffect, useMemo, useState} from "react";
import SearchInput from "@/components/ui/SearchInput.tsx";
import TagWithDescriptionButton from "@/components/ui/TagWithDescriptionButton.tsx";
import {ScrollArea} from "@/components/ui/scroll-area"
import {Button} from "@/components/ui/button.tsx";
import $api from "@/http";
import {toast} from "@/hooks/use-toast.ts";

// interface ITag{
//     name:string;
//     description:string;
// }


//
// const parkingTags: ITag[] = [
//     { name: "🚗 Covered", description: "🏡 Keep your car dry and cozy under a roof!" },
//     { name: "☀️ Open Air", description: "🌳 Enjoy the fresh air while parking under the sky." },
//     { name: "⚡ Electric Charging", description: "🔋 Charge up your EV with on-site charging stations." },
//     { name: "♿ Accessible", description: "✨ Convenient spaces for everyone’s needs." },
//     { name: "🎩 Valet", description: "🚘 Let someone else do the parking for you in style." },
//     { name: "🔒 Secure", description: "🛡️ Safety first! Monitored and secure parking area." },
//     { name: "🚗 Compact", description: "➡️ Perfectly snug for smaller vehicles." },
//     { name: "👨‍👩‍👧‍👦 Family", description: "🚘 Extra room for families with kids in tow." },
//     { name: "🏍️ Motorcycle", description: "📍 Dedicated spots for two-wheelers." },
//     { name: "🚴‍♂️ Bicycle", description: "🔐 Secure racks for your bikes." },
//     { name: "⛅ Underground", description: "🚇 Park underground and beat the weather." },
//     { name: "🕒 Long Term", description: "📆 Perfect for extended stays." },
//     { name: "⏱️ Short Term", description: "🛍️ Quick and easy parking for short visits." },
//     { name: "💎 Premium", description: "🚗 Exclusive spots for a VIP parking experience." },
//     { name: "🌱 Eco-Friendly", description: "♻️ Green parking solutions for a sustainable future." },
//     { name: "✈️ Airport", description: "🚘 Hassle-free parking for jet-setters." },
//     { name: "🏨 Hotel", description: "🛎️ Convenient parking right at your stay." },
//     { name: "🛍️ Mall", description: "🚗 Easy access to shopping and fun." },
//     { name: "🏟️ Stadium", description: "🎉 Ready for the big game? Park here!" },
//     { name: "🚉 Nearby Transit", description: "🚌 Park and hop onto public transport." }
// ];

interface IParkingTag {
    id: string;
    name: string;
    description: string;
}

interface IParkingWithTag {
    id: string;
    name: string;
    imageUrl: string;
    address: IAddress;
    capacity: number;
    occupiedSlots: number;
    is24h: boolean;
    tags: IParkingTag[];
    workingTimeList?: IOpeningHours[];
}

function ParkingDetailsTags({parking}: { parking: IParking }) {

    //TODO: add dynamic change disabled value in button (if activeTags == parking.tags)

    // const [activeTags, setActiveTags] = useState<ITag[]>([
    //     { name: "🚗 Covered", description: "🏡 Keep your car dry and cozy under a roof!" },
    //     { name: "☀️ Open Air", description: "🌳 Enjoy the fresh air while parking under the sky." },
    //     { name: "⚡ Electric Charging", description: "🔋 Charge up your EV with on-site charging stations." },
    // ]);

    const [activeTags, setActiveTags] = useState<IParkingTag[]>([] as IParkingTag[]);

    const [allTags, setAllTags] = useState<IParkingTag[]>([]);


    useEffect(() => {
        const fetchData = async () => {
            try {
                console.log("Fetch parking fetch", "53.4525613, 14.5383154")

                const response = await $api.get(`/api/v1/parking/search`, {
                    params: {
                        page: 0,
                    }
                });

                console.log("Response parking fetch", response);

                if (response.status < 210 && Array.isArray(response.data.content)) {
                    const par = response?.data?.content.find(
                        (p: IParkingWithTag) => p.id === parking.id
                    );

                    if (par) {
                        setActiveTags(par.tags);
                    } else {
                        console.warn("Parking with matching ID not found.");
                    }
                }
            } catch (error) {
                console.error("Failed to fetch parking:", error);
            }
        };

        fetchData();
    }, [parking.id]);

    useEffect(() => {
        const fetchData = async () => {
            try {

                const response = await $api.get(`/api/v1/tags/search`);

                console.log("Response parking tags fetch", response);

                if (response.status < 210) {
                    setAllTags(response.data);
                }
            } catch (error) {
                console.error("Failed to fetch parking:", error);
            }
        };

        fetchData();
    }, [parking.id]);

    const [searchQuery, setSearchQuery] = useState('');


    function addTag(tag: IParkingTag) {
        setActiveTags([...activeTags, tag]);
    }

    function removeFromList(tagID: string) {
        setActiveTags((prevTags) => prevTags.filter((tag) => tag.name !== tagID));
    }

    const getAvailableTags = useMemo(() => {
        return allTags.filter(
            (tag) => !activeTags.some((activeTag) => activeTag.name === tag.name)
        );
    }, [activeTags, allTags]);

    //search query for parking filter
    const getFilteredAvailableParkings = useMemo(() => {
        return getAvailableTags
            .filter(tag => tag.name.toLowerCase().includes(searchQuery.toLowerCase())
                || tag.description.toLowerCase().includes(searchQuery.toLowerCase())
            );
    }, [searchQuery, getAvailableTags]);


    const extractTagIds = (tags: IParkingTag[]): string[] => {
        return tags.map(tag => tag.id);
    };

    async function updateTags() {
        const response = await $api.put(`/api/v1/parking/${parking.id}/tags`, extractTagIds(activeTags));

        if (response.status < 210) {
            toast({
                title: "Tags updated",
                description: "Tags was updated successfully.",
                variant: "success",
            })
        } else {
            toast({
                title: "Tags updated",
                description: "Tags was not updated.",
                variant: "destructive",
            })
        }
    }


    return (
        <div className="relative mb-20">
            <div className="flex flex-col justify-start items-start text-left gap-5 border-b-2 p-2 mb-5 label-text ">
                <h3>Tags</h3>
                <p> Here you can manage tags of features, that you parking have</p>
            </div>

            <div className=" flex flex-row flex-wrap gap-3 items-center justify-center">

                {activeTags.map((tag) => (
                    <Tag key={tag.name} value={tag.name} onClose={() => removeFromList(tag.name)}
                         className="fade-out-5"/>
                ))}
            </div>
            <div
                className=" w-full border rounded-xl border-gray-100 shadow pt-[30px] mt-10 flex flex-col justify-center items-center">
                <SearchInput
                    value={searchQuery}
                    onChange={(e: { target: { value: SetStateAction<string>; }; }) => setSearchQuery(e.target.value)}

                    placeholder="Search for tags" inputClassName="bg-white w-full"/>
                <ScrollArea className="h-[500px] w-full rounded-md border p-4 flex flex-col border-t-0 items-center justify-center scroll_area">
                {getFilteredAvailableParkings.map((tag) => (
                    <TagWithDescriptionButton key={tag.name} tag={tag} onClick={() => addTag(tag)}
                         className="fade-out-5 w-full "/>
                ))}
                </ScrollArea>

            </div>
            <Button
                type="button" onClick={updateTags}
                className="bg-lime-600 self-end absolute right-0 bottom-[-70px] text-white"
            >
                Save Tags
            </Button>
        </div>
    )
}

export default ParkingDetailsTags;