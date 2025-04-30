import SubscriptionsDataTable from "@/components/blocks/tables/SubscriptionsDataTable"
import {ITariffRecord} from "@/models/common/ITariffRecord.ts";
import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.tsx";
import {useToast} from "@/hooks/use-toast"
import {IToastInfo, ToastType} from "@/models/common/IToastInfo.ts";
import {useParams} from "react-router-dom";
import SmallLoader from "@/components/ui/small-loader.tsx";
import PageHeader from "@/components/header/PageHeader.tsx";

function Subscriptions() {
    const {parkingId} = useParams<{ parkingId: string }>();

    const [tariffs, setTariffs] = useState<ITariffRecord[]>([]);
    const {tariffStore} = useContext(Context);
    const { toast } = useToast();

    const [loading, setLoading] = useState<boolean>(false);


    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                if (!parkingId) {
                    console.warn("No parkingId passed");
                    return;
                }
                const toastMessage: IToastInfo | void = await tariffStore.fetchTariffData(parkingId);

                if (toastMessage) {
                    console.log(toastMessage);
                }
                setTariffs(tariffStore.tariffs);
                console.log("Tariffs")
                console.log(tariffs);
            } catch (error) {
                console.error("Failed to fetch tariffs:", error);
            }
            setLoading(false);
        };

        fetchData();
    }, [parkingId, tariffStore]);

    useEffect(() => {
        console.log(tariffs);
    }, [tariffs]);

    const handleCreateTariff = (tariff: ITariffRecord) => {
        if (!parkingId) {
            console.error("Cannot create tariff: parkingId is undefined");
            return;
        }
        tariffStore.createTariff(tariff, parkingId).then(
            (toastMessage) => {
                console.log(toastMessage);
                if (toastMessage) {
                    toast({
                        title: "Tariff creation",
                        description: toastMessage.message,
                        variant: toastMessage.type == ToastType.ERROR ? "destructive" : "default"
                    })
                }
                setTariffs(tariffStore.tariffs);
            });
    }



    return (
        <div className="min-h-screen bg-neutral-100 text-gray-800 m-8">
            <PageHeader title="Manage Your Parking Tariffs" subtitle="Easily create, view, and manage your parking tariffs in one place. Use this page to define rates, update pricing strategies, or remove outdated tariffs. Streamline your parking lot's operation and ensure customers always know what to expect when they park."/>
                <section className="bg-white shadow-lg rounded-lg p-6 mt-6 md:p-10">
                    { loading
                        ? <SmallLoader className="min-h-[300px]"/>
                        : <SubscriptionsDataTable data={tariffs} onAddTariff={handleCreateTariff}/>
                    }
                </section>
        </div>
    );
}

export default Subscriptions;